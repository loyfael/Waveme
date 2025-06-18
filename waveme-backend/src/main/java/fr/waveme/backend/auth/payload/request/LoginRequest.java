package fr.waveme.backend.auth.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * LoginRequest is a class that represents the request payload for user login.
 * It contains fields for the user's pseudo and password, both of which are required.
 * This class is used to validate the input data when a user attempts to log in.
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank
    private String pseudo;

    @NotBlank
    private String password;

    public void setUsername(String user) {
        this.pseudo = user;
    }

    public String getUsername() {
        return pseudo;
    }
}
