package fr.waveme.backend.crud.repository;

import fr.waveme.backend.crud.models.Reply;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends MongoRepository<Reply, Long> {
    Optional<Reply> findByDescription(String description);
    Optional<Reply> findByDescriptionContaining(String partOfDescription);
    List<Reply> findByUserId(String userId);
}
