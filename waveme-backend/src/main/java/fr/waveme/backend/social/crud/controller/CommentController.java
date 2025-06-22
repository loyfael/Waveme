package fr.waveme.backend.social.crud.controller;

import fr.waveme.backend.social.crud.dto.pub.comment.CommentPublicDto;
import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.models.reaction.CommentVote;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.repository.react.CommentVoteRepository;
import fr.waveme.backend.social.crud.sequence.SequenceGeneratorService;
import fr.waveme.backend.social.crud.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * CommentController handles the CRUD operations for comments on posts,
 * including adding comments and voting on comments.
 */
@RestController
@RequestMapping("/api/comments")
public class CommentController {

  private final CommentService commentService;

  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @PostMapping("/{postUniqueId}")
  public Comment addCommentToPost(
          @PathVariable Long postUniqueId,
          @RequestParam String content,
          @RequestHeader("Authorization") String authorizationHeader
  ) {
    String token = authorizationHeader.replace("Bearer ", "");
    return commentService.addCommentToPost(postUniqueId, content, token);
  }

  @PostMapping("/{commentUniqueId}/vote")
  public ResponseEntity<?> voteComment(
          @PathVariable Long commentUniqueId,
          @RequestParam boolean upvote,
          @RequestHeader("Authorization") String authorizationHeader
  ) {
    String token = authorizationHeader.replace("Bearer ", "");
    return commentService.voteComment(commentUniqueId, upvote, token);
  }

  @GetMapping("/{commentUniqueId}/votes")
  public ResponseEntity<?> getCommentVotes(@PathVariable Long commentUniqueId) {
    return commentService.getCommentVotes(commentUniqueId);
  }

  @GetMapping("/getall/{postUniqueId}")
  public ResponseEntity<List<CommentPublicDto>> getCommentsByPostId(@PathVariable Long postUniqueId) {
    return commentService.getCommentsByPostId(postUniqueId);
  }
}
