package fr.waveme.backend.crud.repository;

import fr.waveme.backend.crud.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByDescription(String description);
    Optional<Comment> findByDescriptionContaining(String partOfDescription);
}
