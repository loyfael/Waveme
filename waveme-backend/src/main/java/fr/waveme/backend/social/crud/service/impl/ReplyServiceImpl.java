package fr.waveme.backend.social.crud.service.impl;

import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.dto.ReplyDto;
import fr.waveme.backend.social.crud.dto.pub.reply.ReplyPublicDto;
import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.models.Reply;
import fr.waveme.backend.social.crud.models.reaction.ReplyVote;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.ReplyRepository;
import fr.waveme.backend.social.crud.repository.react.ReplyVoteRepository;
import fr.waveme.backend.social.crud.sequence.SequenceGeneratorService;
import fr.waveme.backend.social.crud.service.ReplyService;
import fr.waveme.backend.utils.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * ReplyServiceImpl provides the implementation of the ReplyService interface.
 * It contains methods to create, retrieve, update, and delete replies.
 */
@Service
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private SequenceGeneratorService sequenceGenerator;
    @Autowired private ReplyVoteRepository replyVoteRepository;

    @Override
    public ResponseEntity<Reply> addReplyToComment(Long commentUniqueId, String content, String authorizationHeader, String ipAddress) {
        ipAddress = ipAddress != null ? ipAddress : "unknown";
        RateLimiter.checkRateLimit("reply:" + ipAddress);

        String userId = jwtUtils.getSocialUserIdFromJwtToken(authorizationHeader.replace("Bearer ", ""));

        Comment comment = commentRepository.findByCommentUniqueId(commentUniqueId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Comment not found"));

        Reply reply = new Reply();
        reply.setReplyUniqueId(sequenceGenerator.generateSequence("reply_sequence"));
        reply.setUserId(userId);
        reply.setCommentId(comment.getCommentUniqueId());
        reply.setDescription(content);
        reply.setUpVote(0);
        reply.setDownVote(0);
        reply.setCreatedAt(LocalDateTime.now());
        reply.setUpdatedAt(LocalDateTime.now());

        return ResponseEntity.ok(replyRepository.save(reply));
    }

    @Override
    public ResponseEntity<String> voteReply(Long replyUniqueId, boolean upvote, String authorizationHeader) {
        String userId = jwtUtils.getSocialUserIdFromJwtToken(authorizationHeader.replace("Bearer ", ""));

        if (replyVoteRepository.existsByReplyIdAndUserId(replyUniqueId, userId)) {
            return ResponseEntity.status(FORBIDDEN).body("Vous avez déjà voté pour cette réponse.");
        }

        Reply reply = replyRepository.findByReplyUniqueId(replyUniqueId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Reply not found"));

        ReplyVote vote = new ReplyVote();
        vote.setReplyId(replyUniqueId);
        vote.setUserId(userId);
        vote.setUpvote(upvote);
        replyVoteRepository.save(vote);

        if (upvote) reply.setUpVote(reply.getUpVote() + 1);
        else reply.setDownVote(reply.getDownVote() + 1);

        replyRepository.save(reply);
        return ResponseEntity.ok("Vote enregistré");
    }

    @Override
    public ResponseEntity<?> getReplyVotes(Long replyUniqueId) {
        Reply reply = replyRepository.findByReplyUniqueId(replyUniqueId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Reply not found"));

        return ResponseEntity.ok(Map.of(
                "upVote", reply.getUpVote(),
                "downVote", reply.getDownVote()
        ));
    }

    @Override
    public ResponseEntity<List<ReplyPublicDto>> getRepliesByCommentId(Long commentUniqueId) {
        List<ReplyPublicDto> replies = replyRepository.findAllByCommentId(commentUniqueId).stream()
                .map(reply -> new ReplyPublicDto(
                        reply.getId(),
                        reply.getDescription(),
                        reply.getUpVote(),
                        reply.getDownVote(),
                        reply.getUserId(),
                        reply.getCreatedAt() != null
                                ? reply.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()
                                : Instant.EPOCH
                ))
                .toList();

        return ResponseEntity.ok(replies);
    }
}
