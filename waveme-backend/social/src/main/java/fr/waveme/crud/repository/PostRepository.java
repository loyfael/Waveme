package fr.waveme.crud.repository;

import fr.waveme.crud.models.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends MongoRepository<Post, Long> {
    Optional<Post> findByImageUrl(String imageUrl);
    Optional<Post> findByImageUrlContaining(String partOfImageUrl);
    List<Post> findByUserId(String userId);
}
