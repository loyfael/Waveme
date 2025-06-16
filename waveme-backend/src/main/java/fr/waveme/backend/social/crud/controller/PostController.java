package fr.waveme.backend.social.crud.controller;

import fr.waveme.backend.social.crud.dto.pub.*;
import fr.waveme.backend.social.crud.dto.pub.comment.CommentAndUserPublicDto;
import fr.waveme.backend.social.crud.dto.pub.post.PostPublicDto;
import fr.waveme.backend.social.crud.dto.pub.reply.ReplyAndUserPublicDto;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.models.UserProfile;
import fr.waveme.backend.social.crud.models.reaction.PostVote;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.social.crud.repository.ReplyRepository;
import fr.waveme.backend.social.crud.repository.UserProfileRepository;
import fr.waveme.backend.social.crud.repository.react.PostVoteRepository;
import fr.waveme.backend.social.crud.sequence.SequenceGeneratorService;
import fr.waveme.backend.social.crud.service.MinioService;
import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.utils.RateLimiter;
import fr.waveme.backend.utils.UrlShorter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

/**
 * PostController handles the CRUD operations for posts, including image uploads,
 * downloads, voting, and fetching posts by user ID.
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final MinioService minioService;
    private final PostRepository postRepository;
    private final PostVoteRepository postVoteRepository;
    private final JwtUtils jwtUtils;
    private final SequenceGeneratorService sequenceGenerator;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final UserProfileRepository userProfileRepository;

    public PostController(
            MinioService minioService,
            PostRepository postRepository,
            PostVoteRepository postVoteRepository,
            JwtUtils jwtUtils,
            SequenceGeneratorService sequenceGeneratorService,
            CommentRepository commentRepository,
            ReplyRepository replyRepository,
            UserProfileRepository userProfileRepository
    ) {
        this.minioService = minioService;
        this.postRepository = postRepository;
        this.postVoteRepository = postVoteRepository;
        this.jwtUtils = jwtUtils;
        this.sequenceGenerator = sequenceGeneratorService;
        this.commentRepository = commentRepository;
        this.replyRepository = replyRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadPostImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("bucket") String bucketName,
            @RequestParam("description") String description,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress
    ) {
        try {
            ipAddress = ipAddress != null ? ipAddress : "unknown";
            RateLimiter.checkRateLimit("post:" + ipAddress);

            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is missing");
            }

            logger.info("Received file: {}, bucket: {}", file.getOriginalFilename(), bucketName);

            String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
            String userId = jwtUtils.getSocialUserIdFromJwtToken(token);

            Post post = new Post();
            post.setUserId(userId);
            post.setPostUniqueId(sequenceGenerator.generateSequence("post_sequence"));
            post.setDescription(description);
            post.setUpVote(0);
            post.setDownVote(0);
            post.setCreatedAt(LocalDateTime.now());
            post.setUpdatedAt(LocalDateTime.now());

            // ✅ Upload l’image
            String url = minioService.uploadImage(file, bucketName, post);
            logger.info("File uploaded successfully to MinIO. URL: {}", url);

            // ✅ Définir l’image et sauvegarder
            post.setImageUrl(url);
            postRepository.save(post);

            UrlShorter urlShorter = new UrlShorter();
            String shortUrl = urlShorter.generateShortUrl(url);

            return ResponseEntity.ok(shortUrl);
        } catch (Exception e) {
            logger.error("Error during file upload: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during file upload: " + e.getMessage());
        }
    }

    @GetMapping("/get/{postUniqueId}")
    public ResponseEntity<PostPublicDto> getPostMetadata(
            @PathVariable("postUniqueId") Long postUniqueId
    ) {
        Post post = postRepository.findByPostUniqueId(postUniqueId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        UserProfile userProfile = userProfileRepository.findById(post.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User profile not found"));

        UserSimpleInfoDto userDto = new UserSimpleInfoDto(
                userProfile.getId(),
                userProfile.getPseudo(),
                userProfile.getProfileImg()
        );

        List<CommentAndUserPublicDto> commentDtos = commentRepository.findAllByPostId(post.getPostUniqueId()).stream()
                .map(comment -> {
                    // ⬇️ Get user info for comment author
                    UserProfile commentAuthor = userProfileRepository.findById(comment.getUserId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User profile not found"));

                    UserSimpleInfoDto commentUserDto = new UserSimpleInfoDto(
                            commentAuthor.getId(),
                            commentAuthor.getPseudo(),
                            commentAuthor.getProfileImg()
                    );

                    List<ReplyAndUserPublicDto> replyDtos = replyRepository.findAllByCommentId(comment.getCommentUniqueId()).stream()
                            .map(reply -> {
                                // ⬇️ Get user info for reply author
                                UserProfile replyAuthor = userProfileRepository.findById(reply.getUserId())
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User profile not found"));

                                UserSimpleInfoDto replyUserDto = new UserSimpleInfoDto(
                                        replyAuthor.getId(),
                                        replyAuthor.getPseudo(),
                                        replyAuthor.getProfileImg()
                                );

                                return new ReplyAndUserPublicDto(
                                        reply.getId(),
                                        reply.getDescription(),
                                        reply.getUpVote(),
                                        reply.getDownVote(),
                                        reply.getUserId(),
                                        reply.getCreatedAt() != null
                                                ? reply.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()
                                                : Instant.EPOCH,
                                        replyUserDto
                                );
                            }).toList();

                    return new CommentAndUserPublicDto(
                            comment.getId(),
                            comment.getDescription(),
                            comment.getUpVote(),
                            comment.getDownVote(),
                            comment.getUserId(),
                            comment.getCreatedAt() != null
                                    ? comment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()
                                    : Instant.EPOCH,
                            commentUserDto,
                            replyDtos
                    );
                }).toList();

        PostPublicDto dto = new PostPublicDto(
                post.getPostUniqueId(),
                post.getDescription(),
                "/api/image/get/" + post.getId(),
                post.getCreatedAt() != null
                        ? post.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()
                        : Instant.EPOCH,
                post.getUpVote(),
                post.getDownVote(),
                post.getUpVote() - post.getDownVote(),
                userDto,
                commentDtos
        );

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{postUniqueId}/vote")
    public ResponseEntity<?> votePost(
            @PathVariable Long postUniqueId,
            @RequestParam boolean upvote,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = authorizationHeader.replace("Bearer ", "");
        String userId = jwtUtils.getSocialUserIdFromJwtToken(token);

        if (postVoteRepository.existsByPostIdAndUserId(postUniqueId, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous avez déjà voté pour ce post.");
        }

        Post post = postRepository.findByPostUniqueId(postUniqueId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        // Enregistrer le vote
        PostVote vote = new PostVote();
        vote.setPostId(postUniqueId);
        vote.setUserId(userId);
        vote.setUpvote(upvote);
        postVoteRepository.save(vote);

        if (upvote) post.setUpVote(post.getUpVote() + 1);
        else post.setDownVote(post.getDownVote() + 1);

        postRepository.save(post);
        return ResponseEntity.ok("Vote enregistré");
    }

    @GetMapping("/{postUniqueId}/votes")
    public ResponseEntity<?> getPostVotes(@PathVariable Long postUniqueId) {
        Post post = postRepository.findByPostUniqueId(postUniqueId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        return ResponseEntity.ok(Map.of(
                "upVote", post.getUpVote(),
                "downVote", post.getDownVote()
        ));
    }
}
