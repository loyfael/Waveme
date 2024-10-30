package fr.waveme.backend.crud.repository;

import fr.waveme.backend.crud.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
