package fr.waveme.backend.crud.repository;

import fr.waveme.backend.crud.models.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, Long> {
    Optional<Comment> findByDescription(String description);
    Optional<Comment> findByDescriptionContaining(String partOfDescription);
    List<Comment> findByUserId(Long userId);
}
