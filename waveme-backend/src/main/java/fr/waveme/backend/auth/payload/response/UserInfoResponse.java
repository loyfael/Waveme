package fr.waveme.backend.auth.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * UserInfoResponse is a class that represents the response payload for user information.
 * It contains fields for user ID, pseudo, email, roles, and JWT token.
 * This class is used to send user details back to the client after successful authentication or profile retrieval.
 */
@Getter
public class UserInfoResponse {
    @Setter
    private Long id;
    private String pseudo;
    private final String email;
    private final List<String> roles;
    private final String jwt;

    public UserInfoResponse(Long id, String pseudo, String email, List<String> roles, String jwt) {
        this.id = id;
        this.pseudo = pseudo;
        this.email = email;
        this.roles = roles;
        this.jwt = jwt;
    }

    public void setUsername(String pseudo) {
        this.pseudo = pseudo;
    }

}
