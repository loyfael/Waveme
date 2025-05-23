package fr.waveme.backend.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class UserInfoResponse {
    @Setter
    private Long id;
    private String pseudo;
    private final String email;
    private final List<String> roles;
    private final String jwtCookie;

    public UserInfoResponse(Long id, String pseudo, String email, List<String> roles, String jwtCookie) {
        this.id = id;
        this.pseudo = pseudo;
        this.email = email;
        this.roles = roles;
        this.jwtCookie = jwtCookie;
    }

    public void setUsername(String pseudo) {
        this.pseudo = pseudo;
    }

}
