package fr.waveme.backend.social.crud.repository.react;

import fr.waveme.backend.social.crud.models.reaction.CommentVote;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CommentVoteRepository extends MongoRepository<CommentVote, String> {
  Optional<CommentVote> findByCommentIdAndUserId(Long commentId, String userId);
  boolean existsByCommentIdAndUserId(Long commentId, String userId);
  List<CommentVote> findAllByCommentId(Long commentId);
}
