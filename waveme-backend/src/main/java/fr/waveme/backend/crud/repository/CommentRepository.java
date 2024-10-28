package fr.waveme.backend.crud.repository;

import fr.waveme.backend.crud.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
