package fr.waveme.backend.social.crud.repository.react;

import fr.waveme.backend.social.crud.models.reaction.ReplyVote;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyVoteRepository extends MongoRepository<ReplyVote, String> {
  Optional<ReplyVote> findByReplyIdAndUserId(Long replyId, String userId);
  boolean existsByReplyIdAndUserId(Long replyId, String userId);
  List<ReplyVote> findAllByReplyId(Long replyId);
}
