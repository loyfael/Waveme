package fr.waveme.backend.social.crud.repository;

import fr.waveme.backend.social.crud.models.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends MongoRepository<Post, String> {
    Optional<Post> findByImageUrl(String imageUrl);
    Optional<Post> findByImageUrlContaining(String partOfImageUrl);
    List<Post> findByUserId(String userId);
}
