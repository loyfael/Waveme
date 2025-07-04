package fr.waveme.backend.social.crud.controller;

import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.dto.UserProfileDto;
import fr.waveme.backend.social.crud.dto.pub.*;
import fr.waveme.backend.social.crud.dto.pub.comment.CommentSummaryDto;
import fr.waveme.backend.social.crud.dto.pub.post.PostOnlyPublicDto;
import fr.waveme.backend.social.crud.dto.pub.post.PostSummaryDto;
import fr.waveme.backend.social.crud.exception.UserNotFoundException;
import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.models.ProfileImage;
import fr.waveme.backend.social.crud.models.UserProfile;
import fr.waveme.backend.social.crud.repository.*;
import fr.waveme.backend.social.crud.service.MinioService;
import fr.waveme.backend.utils.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
  private final MinioService minioService;
  private final ProfileImageRepository profileImageRepository;

  public UserInfoController(
          MinioService minioService,
          PostRepository postRepository,
          CommentRepository commentRepository,
          ReplyRepository replyRepository,
          JwtUtils jwtUtils,
          UserProfileRepository userProfileRepository,
          ProfileImageRepository profileImageRepository
  ) {
    this.postRepository = postRepository;
    this.commentRepository = commentRepository;
    this.replyRepository = replyRepository;
    this.jwtUtils = jwtUtils;
    this.userProfileRepository = userProfileRepository;
    this.minioService = minioService;
    this.profileImageRepository = profileImageRepository;
  }

  /**
   * Retrieves a list of posts made by a specific user.
   *
   * @param id The ID of the user whose posts are to be retrieved.
   * @param authorizationHeader The authorization header containing the JWT token.
   * @param ipAddress The IP address of the requester (optional).
   * @return A response entity containing a list of public post data transfer objects.
   */
  @GetMapping("{id}/posts")
  public ResponseEntity<List<PostOnlyPublicDto>> getUserPosts(
          @PathVariable Long id,
          @RequestHeader("Authorization") String authorizationHeader,
          @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress
  ) {
    ipAddress = ipAddress != null ? ipAddress : "unknown";

    String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
    String userId = jwtUtils.getSocialUserIdFromJwtToken(token);

    List<PostOnlyPublicDto> posts = postRepository.findByUserId(userId).stream()
            .map(post -> {
              UserProfile profile = userProfileRepository.findById(post.getUserId())
                      .orElseThrow(() -> new RuntimeException("User profile not found"));

              UserSimpleInfoDto userDto = new UserSimpleInfoDto(
                      profile.getId(),
                      profile.getPseudo(),
                      profile.getProfileImg()
              );

              return new PostOnlyPublicDto(
                      post.getPostUniqueId(), // public identifier
                      post.getDescription(),
                      "/api/image/get/" + post.getId(), // internal image endpoint
                      post.getCreatedAt() != null
                              ? post.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant()
                              : java.time.Instant.EPOCH,
                      post.getUpVote(),
                      post.getDownVote(),
                      post.getUpVote() - post.getDownVote(), // voteSum
                      userDto
              );
            })
            .toList();

    return ResponseEntity.ok(posts);
  }

  @GetMapping("{id}/comments")
  public ResponseEntity<List<CommentSummaryDto>> getUserComments(
          @PathVariable Long id,
          @RequestHeader("Authorization") String authorizationHeader,
          @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress
  ) {
    ipAddress = ipAddress != null ? ipAddress : "unknown";

    String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
    String userId = jwtUtils.getSocialUserIdFromJwtToken(token);

    List<CommentSummaryDto> comments = commentRepository.findByUserId(userId).stream()
            .map(comment -> {
              UserProfile profile = userProfileRepository.findById(comment.getUserId())
                      .orElseThrow(() -> new RuntimeException("User profile not found"));

              UserSimpleInfoDto userDto = new UserSimpleInfoDto(
                      profile.getId(),
                      profile.getPseudo(),
                      profile.getProfileImg()
              );

              return new CommentSummaryDto(
                      comment.getId(),
                      comment.getCommentUniqueId(),
                      comment.getPostId(),
                      comment.getDescription(),
                      comment.getUpVote(),
                      comment.getDownVote(),
                      comment.getUpVote() - comment.getDownVote(),
                      comment.getCreatedAt() != null
                              ? comment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()
                              : Instant.EPOCH
              );
            })
            .toList();

    return ResponseEntity.ok(comments);
  }

  /**
   * Retrieves the current user's profile information.
   *
   * @param authorizationHeader The authorization header containing the JWT token.
   * @return A response entity containing the user profile data or an error message.
   */
  @GetMapping("me")
  public ResponseEntity<UserProfileDto> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {

    String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
    Long authUserId = Long.valueOf(jwtUtils.getSocialUserIdFromJwtToken(token));
    String userIdStr = String.valueOf(authUserId);

    // Récupération du profil
    UserProfile userSocial = userProfileRepository.findById(userIdStr)
            .orElseThrow(() -> new RuntimeException("User not found in social repository"));

    // Recalculs dynamiques
    int postUpvotes = postRepository.findByUserId(userIdStr).stream()
            .mapToInt(p -> p.getUpVote() != null ? p.getUpVote() : 0).sum();
    int commentUpvotes = commentRepository.findByUserId(userIdStr).stream()
            .mapToInt(c -> c.getUpVote() != null ? c.getUpVote() : 0).sum();
    int replyUpvotes = replyRepository.findByUserId(userIdStr).stream()
            .mapToInt(r -> r.getUpVote() != null ? r.getUpVote() : 0).sum();

    int totalUpvotes = postUpvotes + commentUpvotes + replyUpvotes;
    int totalPosts = postRepository.findByUserId(userIdStr).size();
    int totalComments = commentRepository.findByUserId(userIdStr).size();

    UserProfileDto dto = new UserProfileDto(
            userSocial.getId(),
            userSocial.getAuthUserId(),
            userSocial.getPseudo(),
            userSocial.getEmail(),
            userSocial.getProfileImg(),
            totalUpvotes,
            totalPosts,
            totalComments,
            userSocial.getCreatedAt(),
            userSocial.getUpdatedAt()
    );

    return ResponseEntity.ok(dto);
  }

  /**
   * Retrieves a user profile by ID.
   *
   * @param id The ID of the user.
   * @param authorizationHeader The authorization header containing the JWT token.
   * @param ipAddress The IP address of the requester (optional).
   * @return A response entity containing the user profile data or an error message.
   */
  @GetMapping("/{id}")
  public ResponseEntity<UserSocialPublicDto> getUserById(
          @PathVariable Long id,
          @RequestHeader("Authorization") String authorizationHeader,
          @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress
  ) {
    ipAddress = ipAddress != null ? ipAddress : "unknown";

    String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
    Long requesterId = jwtUtils.getAuthUserIdFromJwtToken(token);

    String userId = String.valueOf(id);

    UserProfile userProfile = userProfileRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found"));

    // Recalcul dynamique des stats
    int totalPosts = postRepository.findByUserId(userId).size();
    int totalComments = commentRepository.findByUserId(userId).size();

    int postUpvotes = postRepository.findByUserId(userId).stream()
            .mapToInt(p -> p.getUpVote() != null ? p.getUpVote() : 0).sum();
    int commentUpvotes = commentRepository.findByUserId(userId).stream()
            .mapToInt(c -> c.getUpVote() != null ? c.getUpVote() : 0).sum();
    int replyUpvotes = replyRepository.findByUserId(userId).stream()
            .mapToInt(r -> r.getUpVote() != null ? r.getUpVote() : 0).sum();
    int totalUpvotes = postUpvotes + commentUpvotes + replyUpvotes;

    // Derniers posts
    List<Post> postEntities = postRepository.findTop3ByUserIdOrderByCreatedAtDesc(userId);
    List<PostSummaryDto> latestPosts = postEntities.isEmpty() ? null :
            postEntities.stream()
                    .map(post -> new PostSummaryDto(
                            post.getId(),
                            post.getPostUniqueId(),
                            "/api/image/get/" + post.getId(),
                            post.getDescription(),
                            post.getUpVote(),
                            post.getDownVote(),
                            post.getUpVote() - post.getDownVote(),
                            post.getCreatedAt() != null
                                    ? post.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()
                                    : Instant.EPOCH
                    ))
                    .toList();

    // Derniers commentaires
    List<Comment> commentEntities = commentRepository.findTop3ByUserIdOrderByCreatedAtDesc(userId);
    List<CommentSummaryDto> latestComments = commentEntities.isEmpty() ? null :
            commentEntities.stream()
                    .map(comment -> new CommentSummaryDto(
                            comment.getId(),
                            comment.getCommentUniqueId(),
                            comment.getPostId(),
                            comment.getDescription(),
                            comment.getUpVote(),
                            comment.getDownVote(),
                            comment.getUpVote() - comment.getDownVote(),
                            comment.getCreatedAt() != null
                                    ? comment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()
                                    : Instant.EPOCH
                    ))
                    .toList();

    UserSocialPublicDto dto = new UserSocialPublicDto(
            userProfile.getId(),
            userProfile.getPseudo(),
            totalPosts,
            totalComments,
            totalUpvotes,
            userProfile.getProfileImg(),
            userProfile.getCreatedAt(),
            userProfile.getUpdatedAt(),
            latestPosts,
            latestComments
    );

    return ResponseEntity.ok(dto);
  }

  /**
   * Uploads a profile image for a user.
   *
   * @param userId            The ID of the user.
   * @param file              The image file to upload.
   * @param bucketName        The name of the MinIO bucket.
   * @param authorizationHeader The authorization header containing the JWT token.
   * @param ipAddress         The IP address of the requester (optional).
   * @return A response entity containing the URL of the uploaded image or an error message.
   */
  @PostMapping("/profile-image")
  public ResponseEntity<String> uploadProfileImage(
          @RequestParam("userId") String userId,
          @RequestParam("file") MultipartFile file,
          @RequestParam("bucket") String bucketName,
          @RequestHeader("Authorization") String authorizationHeader,
          @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress
  ) {
    ipAddress = ipAddress != null ? ipAddress : "unknown";
    RateLimiter.checkRateLimit("profile:" + ipAddress);

    if (file == null || file.isEmpty()) {
      return ResponseEntity.badRequest().body("File is missing");
    }

    String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
    String requesterId = jwtUtils.getSocialUserIdFromJwtToken(token);

    if (!requesterId.equals(userId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized to modify this profile.");
    }

    UserProfile user = userProfileRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    try {
      // ✅ Nom unique du fichier
      String objectName = "profile_" + userId + "_" + System.currentTimeMillis();

      // ✅ Upload vers MinIO
      minioService.uploadRawImage(file, bucketName, objectName);

      // ✅ Sauvegarde dans MongoDB
      ProfileImage imageMeta = new ProfileImage();
      imageMeta.setMinioObjectName(objectName);
      imageMeta.setBucketName(bucketName);

      ProfileImage savedImage = profileImageRepository.save(imageMeta);

      // ✅ Construction de l’URL logique avec l’ID Mongo
      String imageAccessUrl = "/image/get/" + savedImage.getId();

      // ✅ Mise à jour du profil
      user.setProfileImg(imageAccessUrl);
      userProfileRepository.save(user);

      return ResponseEntity.ok(imageAccessUrl);
    } catch (Exception e) {
      logger.error("Error during profile image upload: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Error during profile image upload: " + e.getMessage());
    }
  }
}
