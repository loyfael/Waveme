package fr.waveme.crud.controller;

import fr.waveme.crud.models.Post;
import fr.waveme.crud.repository.PostRepository;
import fr.waveme.crud.service.MinioService;
import fr.waveme.security.utils.SocialJwtUtils;
import fr.waveme.utils.RateLimiter;
import fr.waveme.utils.UrlShorter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/posts")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final MinioService minioService;
    private final PostRepository postRepository;
    private final SocialJwtUtils socialJwtUtils;

    public PostController(
            MinioService minioService,
            PostRepository postRepository,
            SocialJwtUtils socialJwtUtils

            ) {
        this.minioService = minioService;
        this.postRepository = postRepository;
        this.socialJwtUtils = socialJwtUtils;
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
            Long userId = socialJwtUtils.getUserIdFromJwtToken(token);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token: user ID not found");
            }

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
}
