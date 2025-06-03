package fr.waveme.backend.auth.crud.repository;

import fr.waveme.backend.auth.crud.models.enumerators.ERole;
import fr.waveme.backend.auth.crud.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * RoleRepository is a Spring Data JPA repository interface for managing Role entities.
 * It extends JpaRepository to provide CRUD operations and custom query methods.
 * This repository is used to interact with the database for role-related operations.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
