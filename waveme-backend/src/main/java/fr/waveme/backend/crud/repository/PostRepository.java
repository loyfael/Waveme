package fr.waveme.backend.crud.repository;

import fr.waveme.backend.crud.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByImageUrl(String imageUrl);
    Optional<Post> findByImageUrlContaining(String partOfImageUrl);
}
