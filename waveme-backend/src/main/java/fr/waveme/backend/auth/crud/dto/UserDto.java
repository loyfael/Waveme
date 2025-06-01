package fr.waveme.backend.auth.crud.dto;

import fr.waveme.backend.social.crud.models.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

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
