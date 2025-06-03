package fr.waveme.backend.auth.crud.repository;

import fr.waveme.backend.auth.crud.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserRepository is a Spring Data JPA repository interface for managing User entities.
 * It extends JpaRepository to provide CRUD operations and custom query methods.
 * This repository is used to interact with the database for user-related operations.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPseudo(String pseudo);

    Boolean existsByPseudo(String username);
    Boolean existsByEmail(String email);
}
