package fr.waveme.backend.auth.crud.repository;

import fr.waveme.backend.auth.crud.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPseudo(String pseudo);

    Boolean existsByPseudo(String username);
    Boolean existsByEmail(String email);
}
