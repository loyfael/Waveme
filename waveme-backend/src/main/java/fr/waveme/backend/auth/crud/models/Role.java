package fr.waveme.backend.auth.crud.models;

import fr.waveme.backend.auth.crud.models.enumerators.ERole;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Role is an entity class that represents a user role in the application.
 * It contains fields for the role's ID and name, which is an enumerated type ERole.
 * This class is used to manage user roles in the authentication module.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;
}
