package fr.waveme.backend.social.crud.service;

import fr.waveme.backend.social.crud.dto.PostDto;
import fr.waveme.backend.social.crud.dto.pub.post.PostPublicDto;
import fr.waveme.backend.social.crud.models.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * PostService provides methods to perform CRUD operations on Post entities.
 * It contains methods to create, retrieve, update, and delete posts.
 */
public interface PostService {

    ResponseEntity<String> uploadPostImage(MultipartFile file, String bucketName, String description, String token);

    ResponseEntity<PostPublicDto> getPostMetadata(Long postUniqueId);
    ResponseEntity<String> votePost(Long postUniqueId, boolean upvote, String authorizationHeader);
    ResponseEntity<?> getPostVotes(Long postUniqueId);
}
