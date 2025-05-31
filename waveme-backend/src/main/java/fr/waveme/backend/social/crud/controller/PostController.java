package fr.waveme.backend.social.crud.controller;

import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.social.crud.repository.ReplyRepository;
import fr.waveme.backend.social.crud.service.MinioService;
import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.utils.RateLimiter;
import fr.waveme.backend.utils.UrlShorter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final MinioService minioService;
    private final PostRepository postRepository;
    private final JwtUtils jwtUtils;

    public PostController(
            MinioService minioService,
            PostRepository postRepository,
            JwtUtils jwtUtils
    ) {
        this.minioService = minioService;
        this.postRepository = postRepository;
        this.jwtUtils = jwtUtils;
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
            String userId = jwtUtils.getUserIdFromJwtToken(token);

            Post post = new Post();
            post.setUserId(userId);
            post.setDescription(description);
            post.setUpVote(0);
            post.setDownVote(0);

            String url = minioService.uploadImage(file, bucketName, post);
            logger.info("File uploaded successfully to MinIO. URL: {}", url);

            UrlShorter urlShorter = new UrlShorter();
            String shortUrl = urlShorter.generateShortUrl(url);

            return ResponseEntity.ok(shortUrl);
        } catch (Exception e) {
            logger.error("Error during file upload: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during file upload: " + e.getMessage());
        }
    }

    @GetMapping("/download-image")
    public ResponseEntity<byte[]> downloadImage(
            @RequestParam("objectName") String objectName,
            @RequestParam("bucket") String bucketName,
            @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress
    ) {
        try {
            ipAddress = ipAddress != null ? ipAddress : "unknown";
            RateLimiter.checkRateLimit("post:" + ipAddress);

            postRepository.findByImageUrlContaining(objectName)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

            try (InputStream inputStream = minioService.downloadImage(bucketName, objectName)) {
                byte[] imageBytes = inputStream.readAllBytes();

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + objectName + "\"")
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(imageBytes);

            } catch (Exception e) {
                logger.error("Error during image download: {}", e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(("Error during image download: " + e.getMessage()).getBytes());
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/{postId}/vote")
    public ResponseEntity<?> votePost(
            @PathVariable String postId,
            @RequestParam boolean upvote,
            @RequestHeader(value = "X-Forwarded-For", required = false) String clientIp
    ) {
        clientIp = clientIp != null ? clientIp : "unknown";
        RateLimiter.checkRateLimit("post:" + postId + ":" + clientIp);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (upvote) post.setUpVote(post.getUpVote() + 1);
        else post.setDownVote(post.getDownVote() + 1);

        postRepository.save(post);
        return ResponseEntity.ok("Vote recorded");
    }
}
