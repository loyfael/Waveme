package fr.waveme.backend.social.crud.controller;

import fr.waveme.backend.social.crud.dto.pub.reply.ReplyPublicDto;
import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.models.Reply;
import fr.waveme.backend.social.crud.models.reaction.ReplyVote;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.ReplyRepository;
import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.repository.react.ReplyVoteRepository;
import fr.waveme.backend.social.crud.sequence.SequenceGeneratorService;
import fr.waveme.backend.social.crud.service.ReplyService;
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
  private final ReplyService replyService;

  public ReplyController(ReplyService replyService) {
    this.replyService = replyService;
  }

  @PostMapping("/{commentUniqueId}")
  public ResponseEntity<Reply> addReplyToComment(
          @PathVariable Long commentUniqueId,
          @RequestParam String content,
          @RequestHeader("Authorization") String authorizationHeader,
          @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress
  ) {
    return replyService.addReplyToComment(commentUniqueId, content, authorizationHeader, ipAddress);
  }

  @PostMapping("/{replyUniqueId}/vote")
  public ResponseEntity<String> voteReply(
          @PathVariable Long replyUniqueId,
          @RequestParam boolean upvote,
          @RequestHeader("Authorization") String authorizationHeader
  ) {
    return replyService.voteReply(replyUniqueId, upvote, authorizationHeader);
  }

  @GetMapping("/{replyUniqueId}/votes")
  public ResponseEntity<?> getReplyVotes(@PathVariable Long replyUniqueId) {
    return replyService.getReplyVotes(replyUniqueId);
  }

  @GetMapping("/getall/{commentUniqueId}")
  public ResponseEntity<List<ReplyPublicDto>> getRepliesByCommentId(@PathVariable Long commentUniqueId) {
    return replyService.getRepliesByCommentId(commentUniqueId);
  }
}

