package fr.waveme.backend.social.crud.controller;

import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.utils.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * CommentController handles the CRUD operations for comments on posts,
 * including adding comments and voting on comments.
 */
@RestController
@RequestMapping("/api/comments")
public class CommentController {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final JwtUtils jwtUtils;

  public CommentController(
          CommentRepository commentRepository,
          PostRepository postRepository,
          JwtUtils jwtUtils
  ) {
    this.commentRepository = commentRepository;
    this.postRepository = postRepository;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping("/{postId}")
  public Comment addCommentToPost(
          @PathVariable Long postId,
          @RequestParam String content,
          @RequestHeader("Authorization") String authorizationHeader,
          @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress
  ) {
    ipAddress = ipAddress != null ? ipAddress : "unknown";
    RateLimiter.checkRateLimit("post:" + ipAddress);

    String userId = jwtUtils.getSocialUserIdFromJwtToken(authorizationHeader.replace("Bearer ", "")); // Si tu stockes userId en String
    postRepository.findByPostUniqueId(postId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

    Comment comment = new Comment();
    comment.setUserId(userId);
    comment.setPostId(postId);
    comment.setDescription(content);
    comment.setUpVote(0);
    comment.setDownVote(0);
    return commentRepository.save(comment);
  }

  @PostMapping("/{commentId}/vote")
  public ResponseEntity<?> voteComment(
          @PathVariable Long commentId,
          @RequestParam boolean upvote,
          @RequestHeader(value = "X-Forwarded-For", required = false) String clientIp
  ) {
    clientIp = clientIp != null ? clientIp : "unknown";
    RateLimiter.checkRateLimit("comment:" + commentId + ":" + clientIp);

    Comment comment = commentRepository.findById(commentId.toString()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
    if (upvote) comment.setUpVote(comment.getUpVote() + 1);
    else comment.setDownVote(comment.getDownVote() + 1);

    commentRepository.save(comment);
    return ResponseEntity.ok("Vote recorded");
  }
}
