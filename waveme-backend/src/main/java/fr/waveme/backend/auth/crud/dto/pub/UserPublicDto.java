package fr.waveme.backend.auth.crud.dto.pub;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPublicDto {
  public Long id;
  public String pseudo;
  public String email;
  public LocalDateTime createdAt;
  public LocalDateTime updatedAt;
}
