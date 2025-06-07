package fr.waveme.backend.social.crud.repository.react;

import fr.waveme.backend.social.crud.models.reaction.PostVote;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PostVoteRepository extends MongoRepository<PostVote, String> {
  Optional<PostVote> findByPostIdAndUserId(Long postId, String userId);
  boolean existsByPostIdAndUserId(Long postId, String userId);
  List<PostVote> findAllByPostId(Long postId);
}
