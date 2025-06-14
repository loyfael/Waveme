package fr.waveme.backend.social.crud.controller;

import fr.waveme.backend.social.crud.dto.pub.ReplyPublicDto;
import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.models.Reply;
import fr.waveme.backend.social.crud.models.reaction.ReplyVote;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.ReplyRepository;
import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.repository.react.ReplyVoteRepository;
import fr.waveme.backend.social.crud.sequence.SequenceGeneratorService;
import fr.waveme.backend.utils.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

/**
 * ReplyController handles the CRUD operations for replies to comments,
 * including adding replies and voting on replies.
 */
@RestController
@RequestMapping("/api/replies")
public class ReplyController {

  private final ReplyRepository replyRepository;
  private final CommentRepository commentRepository;
  private final JwtUtils jwtUtils;
  private final SequenceGeneratorService sequenceGenerator;
  private final ReplyVoteRepository replyVoteRepository;

  public ReplyController(
          ReplyRepository replyRepository,
          CommentRepository commentRepository,
          JwtUtils jwtUtils,
          SequenceGeneratorService sequenceGenerator,
          ReplyVoteRepository replyVoteRepository
  ) {
    this.replyRepository = replyRepository;
    this.commentRepository = commentRepository;
    this.jwtUtils = jwtUtils;
    this.sequenceGenerator = sequenceGenerator;
    this.replyVoteRepository = replyVoteRepository;
  }


  @PostMapping("/{commentUniqueId}")
  public Reply addReplyToComment(
          @PathVariable Long commentUniqueId,
          @RequestParam String content,
          @RequestHeader("Authorization") String authorizationHeader,
          @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress
  ) {
    ipAddress = ipAddress != null ? ipAddress : "unknown";
    RateLimiter.checkRateLimit("reply:" + ipAddress);

    String userId = jwtUtils.getSocialUserIdFromJwtToken(authorizationHeader.replace("Bearer ", ""));

    Comment comment = commentRepository.findByCommentUniqueId(commentUniqueId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

    Reply reply = new Reply();
    reply.setReplyUniqueId(sequenceGenerator.generateSequence("reply_sequence")); // 🆕
    reply.setUserId(userId);
    reply.setCommentId(comment.getCommentUniqueId());
    reply.setDescription(content);
    reply.setUpVote(0);
    reply.setDownVote(0);
    reply.setCreatedAt(LocalDateTime.now());
    reply.setUpdatedAt(LocalDateTime.now());

    return replyRepository.save(reply);
  }

  @PostMapping("/{replyUniqueId}/vote")
  public ResponseEntity<?> voteReply(
          @PathVariable Long replyUniqueId,
          @RequestParam boolean upvote,
          @RequestHeader("Authorization") String authorizationHeader
  ) {
    String token = authorizationHeader.replace("Bearer ", "");
    String userId = jwtUtils.getSocialUserIdFromJwtToken(token);

    if (replyVoteRepository.existsByReplyIdAndUserId(replyUniqueId, userId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous avez déjà voté pour cette réponse.");
    }

    Reply reply = replyRepository.findByReplyUniqueId(replyUniqueId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply not found"));

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

  @GetMapping("/{replyUniqueId}/votes")
  public ResponseEntity<?> getReplyVotes(@PathVariable Long replyUniqueId) {
    Reply reply = replyRepository.findByReplyUniqueId(replyUniqueId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply not found"));

    return ResponseEntity.ok(Map.of(
            "upVote", reply.getUpVote(),
            "downVote", reply.getDownVote()
    ));
  }

  @GetMapping("/getall/{commentUniqueId}")
  public ResponseEntity<List<ReplyPublicDto>> getRepliesByCommentId(@PathVariable Long commentUniqueId) {
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

