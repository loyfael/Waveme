package fr.waveme.backend.auth.crud.repository;

import fr.waveme.backend.auth.crud.models.enumerators.ERole;
import fr.waveme.backend.auth.crud.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
