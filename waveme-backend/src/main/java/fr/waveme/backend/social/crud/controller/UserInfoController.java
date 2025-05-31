package fr.waveme.backend.social.crud.controller;

import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.dto.pub.PostPublicDto;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.social.crud.repository.ReplyRepository;
import fr.waveme.backend.social.crud.service.MinioService;
import fr.waveme.backend.utils.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserInfoController {

  private static final Logger logger = LoggerFactory.getLogger(PostController.class);
  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final ReplyRepository replyRepository;
  private final JwtUtils jwtUtils;

  public UserInfoController(
          MinioService minioService,
          PostRepository postRepository,
          CommentRepository commentRepository,
          ReplyRepository replyRepository,
          JwtUtils jwtUtils
  ) {
    this.postRepository = postRepository;
    this.commentRepository = commentRepository;
    this.replyRepository = replyRepository;
    this.jwtUtils = jwtUtils;
  }

  @GetMapping("{id}/posts")
  public ResponseEntity<List<PostPublicDto>> getUserPosts(
          @PathVariable Long id,
          @RequestHeader("Authorization") String authorizationHeader,
          @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress
  ) {

    ipAddress = ipAddress != null ? ipAddress : "unknown";
    RateLimiter.checkRateLimit("post:" + ipAddress);

    String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
    String userId = jwtUtils.getUserIdFromJwtToken(token);

    List<PostPublicDto> posts = postRepository.findByUserId(userId).stream()
            .map(post -> new PostPublicDto(
                    post.getId(),
                    post.getDescription(),
                    post.getImageUrl(),
                    post.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant()
            ))
            .toList();

    return ResponseEntity.ok(posts);
  }

//  @GetMapping("me")
//  public ResponseEntity<UserPublicDto> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
//
//    String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
//    Long userId = jwtUtils.getUserIdFromJwtToken(token);
//
//    if (userId == null) {
//      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token: user ID not found");
//    }
//
//    User user = userRepository.findById(userId)
//            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
//
//    int postUpvotes = postRepository.findByUser(user).stream()
//            .mapToInt(p -> p.getUpVote() != null ? p.getUpVote() : 0).sum();
//
//    int commentUpvotes = commentRepository.findByUserId(user.getId()).stream()
//            .mapToInt(c -> c.getUpVote() != null ? c.getUpVote() : 0).sum();
//
//    int replyUpvotes = replyRepository.findByUserId(user.getId().toString()).stream()
//            .mapToInt(r -> r.getUpVote() != null ? r.getUpVote() : 0).sum();
//
//    int totalUpvotes = postUpvotes + commentUpvotes + replyUpvotes;
//    int totalPosts = postRepository.findByUser(user).size();
//
//    UserPublicDto dto = new UserPublicDto(
//            user.getId(),
//            user.getPseudo(),
//            user.getProfileImg(),
//            totalUpvotes,
//            totalPosts,
//            user.getCreatedAt(),
//            user.getUpdatedAt()
//    );
//
//    return ResponseEntity.ok(dto);
//  }
}
