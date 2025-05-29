package fr.waveme.backend.social.crud.repository;

import fr.waveme.backend.social.crud.models.Reply;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends MongoRepository<Reply, Long> {
    Optional<Reply> findById(String id);
    Optional<Reply> findByDescription(String description);
    Optional<Reply> findByDescriptionContaining(String partOfDescription);
    List<Reply> findByUserId(String userId);
}
