package fr.waveme.backend.crud.repository;

import fr.waveme.backend.crud.models.Post;
import fr.waveme.backend.crud.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends MongoRepository<Post, String> {
    Optional<Post> findByImageUrl(String imageUrl);
    Optional<Post> findByImageUrlContaining(String partOfImageUrl);
    List<Post> findByUser(User user);
}
