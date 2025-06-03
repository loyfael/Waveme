package fr.waveme.backend.social.crud.repository;

import fr.waveme.backend.social.crud.models.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * PostRepository provides methods to perform CRUD operations on Post entities.
 * It extends MongoRepository to leverage built-in methods for MongoDB interactions.
 */
public interface PostRepository extends MongoRepository<Post, String> {
    Optional<Post> findByImageUrl(String imageUrl);
    Optional<Post> findByImageUrlContaining(String partOfImageUrl);
    List<Post> findByUserId(String userId);
    List<Post> findAllByUserId(String userId);
    Optional<Post> findByIdAndUserId(String postId, String userId);
}
