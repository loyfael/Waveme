package fr.waveme.backend.crud.repository;

import fr.waveme.backend.crud.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
