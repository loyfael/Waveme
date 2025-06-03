package fr.waveme.backend.auth.payload.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 * RegisterRequest is a class that represents the request payload for user registration.
 * It contains fields for the user's pseudo, email, and password, all of which are required.
 * This class is used to validate the input data when a user attempts to register.
 */
@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String pseudo;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

}
