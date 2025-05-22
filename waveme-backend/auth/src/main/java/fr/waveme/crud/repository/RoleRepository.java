package fr.waveme.crud.repository;

import fr.waveme.crud.models.Role;
import fr.waveme.crud.models.enumerators.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
