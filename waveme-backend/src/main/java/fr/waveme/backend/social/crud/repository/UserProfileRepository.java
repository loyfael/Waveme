package fr.waveme.backend.social.crud.repository;

import fr.waveme.backend.social.crud.models.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * UserProfileRepository provides methods to perform CRUD operations on UserProfile entities.
 * It extends MongoRepository to leverage built-in methods for MongoDB interactions.
 */
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
  Optional<UserProfile> findByAuthUserId(Long authUserId);
  Optional<UserProfile> findByPseudo(String pseudo);
}
