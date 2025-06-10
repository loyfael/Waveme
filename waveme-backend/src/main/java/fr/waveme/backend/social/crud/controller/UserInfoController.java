package fr.waveme.backend.social.crud.controller;

import fr.waveme.backend.auth.crud.dto.pub.UserPublicDto;
import fr.waveme.backend.auth.crud.models.User;
import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.dto.UserProfileDto;
import fr.waveme.backend.social.crud.dto.pub.*;
import fr.waveme.backend.social.crud.exception.UserNotFoundException;
import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.models.UserProfile;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.social.crud.repository.ReplyRepository;
import fr.waveme.backend.social.crud.repository.UserProfileRepository;
import fr.waveme.backend.social.crud.service.MinioService;
import fr.waveme.backend.social.crud.service.UserProfileService;
import fr.waveme.backend.utils.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

/**
 * UserInfoController handles user-related operations such as fetching user posts,
 * user profile information, and creating new user profiles.
 */
@RestController
@RequestMapping("/api/user")
public class UserInfoController {

  private static final Logger logger = LoggerFactory.getLogger(PostController.class);
  private final UserProfileRepository userProfileRepository;
  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final ReplyRepository replyRepository;
  private final JwtUtils jwtUtils;
  private final UserProfileService userProfileService;

  public UserInfoController(
          MinioService minioService,
          PostRepository postRepository,
          CommentRepository commentRepository,
          ReplyRepository replyRepository,
          JwtUtils jwtUtils,
          UserProfileRepository userProfileRepository,
          UserProfileService userProfileService
  ) {
    this.postRepository = postRepository;
    this.commentRepository = commentRepository;
    this.replyRepository = replyRepository;
    this.jwtUtils = jwtUtils;
    this.userProfileRepository = userProfileRepository;
    this.userProfileService = userProfileService;
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
    String userId = jwtUtils.getSocialUserIdFromJwtToken(token);

    List<PostPublicDto> posts = postRepository.findByUserId(userId).stream()
            .map(post -> {
              UserProfile profile = userProfileRepository.findById(post.getUserId())
                      .orElseThrow(() -> new RuntimeException("User profile not found"));

              UserInPostPublicDto userDto = new UserInPostPublicDto(
                      profile.getId(),
                      profile.getPseudo(),
                      profile.getProfileImg()
              );

              return new PostPublicDto(
                      post.getId(),
                      post.getDescription(),
                      post.getImageUrl(),
                      post.getCreatedAt() != null
                              ? post.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant()
                              : java.time.Instant.EPOCH,
                      userDto
              );
            })
            .toList();

    return ResponseEntity.ok(posts);
  }

  @GetMapping("me")
  public ResponseEntity<UserProfileDto> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {

    String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
    Long authUserId = Long.valueOf(jwtUtils.getSocialUserIdFromJwtToken(token));

    // Récupération des stats Social
    String userIdStr = String.valueOf(authUserId);
    UserProfile userSocial = userProfileRepository.findById(userIdStr)
            .orElseThrow(() -> new RuntimeException("User not found in social repository"));

    int postUpvotes = postRepository.findByUserId(userIdStr).stream()
            .mapToInt(p -> p.getUpVote() != null ? p.getUpVote() : 0).sum();
    int commentUpvotes = commentRepository.findByUserId(userIdStr).stream()
            .mapToInt(c -> c.getUpVote() != null ? c.getUpVote() : 0).sum();
    int replyUpvotes = replyRepository.findByUserId(userIdStr).stream()
            .mapToInt(r -> r.getUpVote() != null ? r.getUpVote() : 0).sum();
    int totalPosts = postRepository.findByUserId(userIdStr).size();

    UserProfileDto dto = new UserProfileDto(
            userSocial.getId(),
            userSocial.getAuthUserId(),
            userSocial.getPseudo(),
            userSocial.getEmail(),
            userSocial.getProfileImg(),
            userSocial.getTotalUpVotes(),
            userSocial.getTotalPosts(),
            userSocial.getTotalComments(),
            userSocial.getCreatedAt(),
            userSocial.getUpdatedAt()
    );

    return ResponseEntity.ok(dto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserSocialPublicDto> getUserById(
          @PathVariable Long id,
          @RequestHeader("Authorization") String authorizationHeader,
          @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress
  ) {
    ipAddress = ipAddress != null ? ipAddress : "unknown";
    RateLimiter.checkRateLimit("post:" + ipAddress);

    String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
    Long userId = jwtUtils.getAuthUserIdFromJwtToken(token);

    UserProfile userProfile = userProfileRepository.findById(id.toString())
            .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found"));

    // Récupération des 3 derniers posts
    List<Post> postEntities = postRepository.findTop3ByUserIdOrderByCreatedAtDesc(id.toString());
    List<PostSummaryDto> latestPosts = postEntities.isEmpty() ? null :
            postEntities.stream()
                    .map(post -> new PostSummaryDto(
                            post.getId(),
                            post.getImageUrl(),
                            post.getCreatedAt() != null
                                    ? post.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()
                                    : Instant.EPOCH
                    ))
                    .toList();

    // Récupération des 3 derniers commentaires
    List<Comment> commentEntities = commentRepository.findTop3ByUserIdOrderByCreatedAtDesc(id.toString());
    List<CommentSummaryDto> latestComments = commentEntities.isEmpty() ? null :
            commentEntities.stream()
                    .map(comment -> new CommentSummaryDto(
                            comment.getId(),
                            comment.getDescription(),
                            comment.getCreatedAt() != null
                                    ? comment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()
                                    : Instant.EPOCH
                    ))
                    .toList();

    UserSocialPublicDto dto = new UserSocialPublicDto(
            userProfile.getId(),
            userProfile.getPseudo(),
            userProfile.getTotalPosts(),
            userProfile.getTotalComments(),
            userProfile.getTotalUpVotes(),
            userProfile.getProfileImg(),
            userProfile.getCreatedAt(),
            userProfile.getUpdatedAt(),
            latestPosts,
            latestComments
    );

    return ResponseEntity.ok(dto);
  }
}
