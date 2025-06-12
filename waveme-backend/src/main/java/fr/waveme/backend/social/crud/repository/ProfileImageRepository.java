package fr.waveme.backend.social.crud.repository;


import fr.waveme.backend.social.crud.models.ProfileImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImageRepository extends MongoRepository<ProfileImage, String> {
}
