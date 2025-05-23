package fr.waveme.crud.repository;

import fr.waveme.crud.models.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
}

