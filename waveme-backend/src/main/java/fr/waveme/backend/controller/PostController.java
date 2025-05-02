package fr.waveme.backend.controller;

import fr.waveme.backend.crud.models.Comment;
import fr.waveme.backend.crud.models.Post;
import fr.waveme.backend.crud.models.Reply;
import fr.waveme.backend.crud.models.User;
import fr.waveme.backend.crud.repository.CommentRepository;
import fr.waveme.backend.crud.repository.PostRepository;
import fr.waveme.backend.crud.repository.ReplyRepository;
import fr.waveme.backend.crud.repository.UserRepository;
import fr.waveme.backend.crud.service.MinioService;
import fr.waveme.backend.utils.RateLimiter;
import fr.waveme.backend.utils.UrlShorter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final MinioService minioService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    public PostController(
            MinioService minioService,
            UserRepository userRepository,
            PostRepository postRepository,
            CommentRepository commentRepository,
            ReplyRepository replyRepository,
            RateLimiter rateLimiter
    ) {
        this.minioService = minioService;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.replyRepository = replyRepository;
    }

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadPostImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("bucket") String bucketName,
            @RequestParam("userId") Long userId,
            @RequestParam("description") String description,
            @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress
    ) {
        try {
            ipAddress = ipAddress != null ? ipAddress : "unknown";
            RateLimiter.checkRateLimit("post:" + ipAddress);

            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is missing");
            }

            logger.info("Received file: {}, bucket: {}", file.getOriginalFilename(), bucketName);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            Post post = new Post();
            post.setUser(user);
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

    @PostMapping("/{postId}/comments")
    public Comment addCommentToPost(
            @PathVariable Long postId,
            @RequestParam String userId,
            @RequestParam String content,
            @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress
    ) {
        try {
            ipAddress = ipAddress != null ? ipAddress : "unknown";
            RateLimiter.checkRateLimit("post:" + ipAddress);

            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

            Comment comment = new Comment();
            comment.setUserId(userId);
            comment.setDescription(content);
            comment.setPost(post);
            comment.setUpVote(0);
            comment.setDownVote(0);

            return commentRepository.save(comment);
        } catch (Exception e) {
            logger.error("Error adding comment: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding comment");
        }

    }

    @PostMapping("/comments/{commentId}/reply")
    public Reply addReplyToComment(
            @PathVariable Long commentId,
            @RequestParam String userId,
            @RequestParam String content,
            @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress
    ) {
        try {
            ipAddress = ipAddress != null ? ipAddress : "unknown";
            RateLimiter.checkRateLimit("post:" + ipAddress);

            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
            Reply reply = new Reply();

            reply.setUserId(userId);
            reply.setDescription(content);
            reply.setComment(comment);
            reply.setUpVote(0);
            reply.setDownVote(0);

            return replyRepository.save(reply);
        } catch (Exception e) {
            logger.error("Error adding reply: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding reply");
        }
    }

    @PostMapping("/{postId}/vote")
    public ResponseEntity<?> votePost(
            @PathVariable Long postId,
            @RequestParam boolean upvote,
            @RequestHeader(value = "X-Forwarded-For", required = false) String clientIp
    ) {
        clientIp = clientIp != null ? clientIp : "unknown";
        RateLimiter.checkRateLimit("post:" + postId + ":" + clientIp);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (upvote) {
            post.setUpVote(post.getUpVote() + 1);
        } else {
            post.setDownVote(post.getDownVote() + 1);
        }
        postRepository.save(post);
        return ResponseEntity.ok("Vote recorded");
    }

    @PostMapping("/comments/{commentId}/vote")
    public ResponseEntity<?> voteComment(
            @PathVariable Long commentId,
            @RequestParam boolean upvote,
            @RequestHeader(value = "X-Forwarded-For", required = false) String clientIp
    ) {
        clientIp = clientIp != null ? clientIp : "unknown";
        RateLimiter.checkRateLimit("comment:" + commentId + ":" + clientIp);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (upvote) {
            comment.setUpVote(comment.getUpVote() + 1);
        } else {
            comment.setDownVote(comment.getDownVote() + 1);
        }
        commentRepository.save(comment);
        return ResponseEntity.ok("Vote recorded");
    }

    @PostMapping("/replies/{replyId}/vote")
    public ResponseEntity<?> voteReply(
            @PathVariable Long replyId,
            @RequestParam boolean upvote,
            @RequestHeader(value = "X-Forwarded-For", required = false) String clientIp
    ) {
        clientIp = clientIp != null ? clientIp : "unknown";
        RateLimiter.checkRateLimit("reply:" + replyId + ":" + clientIp);

        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply not found"));

        if (upvote) {
            reply.setUpVote(reply.getUpVote() + 1);
        } else {
            reply.setDownVote(reply.getDownVote() + 1);
        }
        replyRepository.save(reply);
        return ResponseEntity.ok("Vote recorded");
    }
}