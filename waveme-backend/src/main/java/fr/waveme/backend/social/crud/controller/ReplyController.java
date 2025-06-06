package fr.waveme.backend.social.crud.controller;

import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.models.Reply;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.ReplyRepository;
import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.sequence.SequenceGeneratorService;
import fr.waveme.backend.utils.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

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

  public ReplyController(
          ReplyRepository replyRepository,
          CommentRepository commentRepository,
          JwtUtils jwtUtils,
          SequenceGeneratorService sequenceGenerator
  ) {
    this.replyRepository = replyRepository;
    this.commentRepository = commentRepository;
    this.jwtUtils = jwtUtils;
    this.sequenceGenerator = sequenceGenerator;
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

  @PostMapping("/{replyId}/vote")
  public ResponseEntity<?> voteReply(
          @PathVariable Long replyId,
          @RequestParam boolean upvote,
          @RequestHeader(value = "X-Forwarded-For", required = false) String clientIp
  ) {
    clientIp = clientIp != null ? clientIp : "unknown";
    RateLimiter.checkRateLimit("reply:" + replyId + ":" + clientIp);

    Reply reply = replyRepository.findById(replyId).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply not found"));
    if (upvote) reply.setUpVote(reply.getUpVote() + 1);
    else reply.setDownVote(reply.getDownVote() + 1);

    replyRepository.save(reply);
    return ResponseEntity.ok("Vote recorded");
  }
}

