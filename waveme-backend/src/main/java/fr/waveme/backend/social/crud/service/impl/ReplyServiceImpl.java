package fr.waveme.backend.social.crud.service.impl;

import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.dto.ReplyDto;
import fr.waveme.backend.social.crud.dto.pub.UserSimpleInfoDto;
import fr.waveme.backend.social.crud.dto.pub.react.ReplyVoteDetailsDto;
import fr.waveme.backend.social.crud.dto.pub.reply.ReplyPublicDto;
import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.models.Reply;
import fr.waveme.backend.social.crud.models.reaction.ReplyVote;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.ReplyRepository;
import fr.waveme.backend.social.crud.repository.UserProfileRepository;
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
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * ReplyServiceImpl provides the implementation of the ReplyService interface.
 * It contains methods to create, retrieve, update, and delete replies.
 */
@Service
public class ReplyServiceImpl implements ReplyService {
    @Autowired private final ReplyRepository replyRepository;
    @Autowired private final CommentRepository commentRepository;
    @Autowired private final JwtUtils jwtUtils;
    @Autowired private final SequenceGeneratorService sequenceGeneratorService;
    @Autowired private final ReplyVoteRepository replyVoteRepository;
    @Autowired private final UserProfileRepository userProfileRepository;

    public ReplyServiceImpl(
            ReplyRepository replyRepository,
            CommentRepository commentRepository,
            JwtUtils jwtUtils,
            SequenceGeneratorService sequenceGeneratorService,
            ReplyVoteRepository replyVoteRepository,
            UserProfileRepository userProfileRepository
    ) {
        this.replyRepository = replyRepository;
        this.commentRepository = commentRepository;
        this.jwtUtils = jwtUtils;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.replyVoteRepository = replyVoteRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public ResponseEntity<Reply> addReplyToComment(Long commentUniqueId, String content, String authorizationHeader, String ipAddress) {
        ipAddress = ipAddress != null ? ipAddress : "unknown";
        RateLimiter.checkRateLimit("reply:" + ipAddress);

        String userId = jwtUtils.getSocialUserIdFromJwtToken(authorizationHeader.replace("Bearer ", ""));

        Comment comment = commentRepository.findByCommentUniqueId(commentUniqueId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Comment not found"));

        Reply reply = new Reply();
        reply.setReplyUniqueId(sequenceGeneratorService.generateSequence("reply_sequence"));
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

        Reply reply = replyRepository.findByReplyUniqueId(replyUniqueId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Reply not found"));

        // Chercher si l'utilisateur a déjà voté sur cette réponse
        Optional<ReplyVote> existingVoteOpt = replyVoteRepository.findByReplyIdAndUserId(replyUniqueId, userId);

        if (existingVoteOpt.isPresent()) {
            ReplyVote existingVote = existingVoteOpt.get();
            
            // L'utilisateur a déjà voté
            if (existingVote.isUpvote() == upvote) {
                // Même type de vote - on annule le vote
                replyVoteRepository.delete(existingVote);
                
                if (upvote) {
                    reply.setUpVote(Math.max(0, reply.getUpVote() - 1));
                } else {
                    reply.setDownVote(Math.max(0, reply.getDownVote() - 1));
                }
                
                replyRepository.save(reply);
                return ResponseEntity.ok("Vote annulé");
            } else {
                // Type de vote différent - on change le vote
                existingVote.setUpvote(upvote);
                replyVoteRepository.save(existingVote);
                
                if (upvote) {
                    // Changement vers upvote
                    reply.setUpVote(reply.getUpVote() + 1);
                    reply.setDownVote(Math.max(0, reply.getDownVote() - 1));
                } else {
                    // Changement vers downvote
                    reply.setDownVote(reply.getDownVote() + 1);
                    reply.setUpVote(Math.max(0, reply.getUpVote() - 1));
                }
                
                replyRepository.save(reply);
                return ResponseEntity.ok("Vote modifié");
            }
        } else {
            // Nouveau vote
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

    @Override
    public ResponseEntity<?> getUserReplyVotes(Long replyUniqueId) {
        Reply reply = replyRepository.findByReplyUniqueId(replyUniqueId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Reply not found"));

        List<ReplyVote> votes = replyVoteRepository.findAllByReplyId(replyUniqueId);

        List<UserSimpleInfoDto> upvoters = votes.stream()
                .filter(ReplyVote::isUpvote)
                .map(ReplyVote::getUserId)
                .map(id -> userProfileRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .map(p -> new UserSimpleInfoDto(p.getId(), p.getPseudo(), p.getProfileImg()))
                .toList();

        List<UserSimpleInfoDto> downvoters = votes.stream()
                .filter(v -> !v.isUpvote())
                .map(ReplyVote::getUserId)
                .map(id -> userProfileRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .map(p -> new UserSimpleInfoDto(p.getId(), p.getPseudo(), p.getProfileImg()))
                .toList();

        return ResponseEntity.ok(new ReplyVoteDetailsDto(
                reply.getUpVote(),
                reply.getDownVote(),
                upvoters,
                downvoters
        ));
    }
}
