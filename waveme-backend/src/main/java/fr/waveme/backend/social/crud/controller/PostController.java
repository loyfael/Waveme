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
import fr.waveme.backend.social.crud.service.PostService;
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

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadPostImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("bucket") String bucket,
            @RequestParam("description") String description,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is missing");
        }
        
        String token = authorizationHeader.replace("Bearer ", "");
        return postService.uploadPostImage(file, bucket, description, token);
    }

    @GetMapping("/get/{postUniqueId}")
    public ResponseEntity<PostPublicDto> getPostMetadata(@PathVariable Long postUniqueId) {
        try {
            return postService.getPostMetadata(postUniqueId);
        } catch (ResponseStatusException e) {
            throw e; // Propager l'exception au lieu de retourner null
        }
    }

    @PostMapping("/{postUniqueId}/vote")
    public ResponseEntity<String> votePost(
            @PathVariable Long postUniqueId,
            @RequestParam boolean upvote,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = authorizationHeader.replace("Bearer ", "");
        return postService.votePost(postUniqueId, upvote, token);
    }

    @GetMapping("/{postUniqueId}/votes")
    public ResponseEntity<?> getPostVotes(@PathVariable Long postUniqueId) {
        return postService.getPostVotes(postUniqueId);
    }

    @GetMapping("/{postUniqueId}/get-user-votes")
    public ResponseEntity<?> getUserPostVotes(@PathVariable Long postUniqueId) {
        return postService.getUserPostVotes(postUniqueId);
    }
}
