package fr.waveme.backend.social.crud.controller;

import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.sequence.SequenceGeneratorService;
import fr.waveme.backend.utils.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

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
  private final SequenceGeneratorService sequenceGenerator;

  public CommentController(
          CommentRepository commentRepository,
          PostRepository postRepository,
          JwtUtils jwtUtils,
          SequenceGeneratorService sequenceGenerator
  ) {
    this.commentRepository = commentRepository;
    this.postRepository = postRepository;
    this.jwtUtils = jwtUtils;
    this.sequenceGenerator = sequenceGenerator;
  }

  @PostMapping("/{postUniqueId}")
  public Comment addCommentToPost(
          @PathVariable Long postUniqueId,
          @RequestParam String content,
          @RequestHeader("Authorization") String authorizationHeader,
          @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress
  ) {
    ipAddress = ipAddress != null ? ipAddress : "unknown";
    RateLimiter.checkRateLimit("post:" + ipAddress);

    String userId = jwtUtils.getSocialUserIdFromJwtToken(authorizationHeader.replace("Bearer ", ""));

    Post post = postRepository.findByPostUniqueId(postUniqueId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

    Comment comment = new Comment();
    comment.setCommentUniqueId(sequenceGenerator.generateSequence("comment_sequence")); // 🆕
    comment.setUserId(userId);
    comment.setPostId(post.getPostUniqueId());
    comment.setDescription(content);
    comment.setUpVote(0);
    comment.setDownVote(0);
    comment.setCreatedAt(LocalDateTime.now());
    comment.setUpdatedAt(LocalDateTime.now());

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
