package fr.waveme.backend.social.crud.controller;

import fr.waveme.backend.social.crud.models.Reply;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.ReplyRepository;
import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.utils.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/replies")
public class ReplyController {

  private final ReplyRepository replyRepository;
  private final CommentRepository commentRepository;
  private final JwtUtils jwtUtils;

  public ReplyController(
          ReplyRepository replyRepository,
          CommentRepository commentRepository,
          JwtUtils jwtUtils
  ) {
    this.replyRepository = replyRepository;
    this.commentRepository = commentRepository;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping("/{commentId}")
  public Reply addReplyToComment(
          @PathVariable Long commentId,
          @RequestParam String content,
          @RequestHeader("Authorization") String authorizationHeader,
          @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress
  ) {
    ipAddress = ipAddress != null ? ipAddress : "unknown";
    RateLimiter.checkRateLimit("reply:" + ipAddress);

    String userId = jwtUtils.getSocialUserIdFromJwtToken(authorizationHeader.replace("Bearer ", ""));
    commentRepository.findById(commentId.toString()).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

    Reply reply = new Reply();
    reply.setUserId(userId);
    reply.setCommentId(commentId);
    reply.setDescription(content);
    reply.setUpVote(0);
    reply.setDownVote(0);
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

