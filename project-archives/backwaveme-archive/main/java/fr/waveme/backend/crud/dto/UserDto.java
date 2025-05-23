package fr.waveme.backend.crud.dto;

import fr.waveme.backend.crud.models.Post;
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
    private String profileImg;
    private Set<Post> posts;
}
