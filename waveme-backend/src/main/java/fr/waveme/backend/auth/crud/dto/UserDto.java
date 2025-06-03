package fr.waveme.backend.auth.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * UserDto is a Data Transfer Object (DTO) that represents a user in the application.
 * It contains fields for user identification, pseudo, email, and password.
 * This DTO is used to transfer user data between different layers of the application,
 * such as from the controller to the service layer.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String pseudo;
    private String email;
    private String password;
}
