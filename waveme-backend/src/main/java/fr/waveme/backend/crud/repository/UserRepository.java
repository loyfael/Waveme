package fr.waveme.backend.crud.repository;

import fr.waveme.backend.crud.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
