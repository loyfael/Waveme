package fr.waveme.backend.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    @NotBlank
    @Size(min = 6, max = 2)
    private String pseudo;

    @NotBlank
    @Size(min = 15, max = 40)
    private String password;
}
