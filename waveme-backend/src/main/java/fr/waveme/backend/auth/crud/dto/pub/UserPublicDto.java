package fr.waveme.backend.auth.crud.dto.pub;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * UserPublicDto is a Data Transfer Object (DTO) that represents a public view of a user.
 * It contains fields for user identification, pseudo, email, and timestamps for creation and update.
 * This DTO is used to transfer user data in a public context, such as in API responses.
 */
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
