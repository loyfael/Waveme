package fr.waveme.backend.social.crud.repository;

import fr.waveme.backend.social.crud.models.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * CommentRepository provides methods to perform CRUD operations on Comment entities.
 * It extends MongoRepository to leverage built-in methods for MongoDB interactions.
 */
public interface CommentRepository extends MongoRepository<Comment, String> {
    Optional<Comment> findByDescription(String description);
    Optional<Comment> findByDescriptionContaining(String partOfDescription);
    List<Comment> findByUserId(String userId);
    Optional<Comment> findByCommentUniqueId(Long commentUniqueId);
    List<Comment> findTop3ByUserIdOrderByCreatedAtDesc(String userId);
    List<Comment> findAllByPostId(Long postUniqueId);
}
