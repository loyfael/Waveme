package fr.waveme.backend.auth.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * Request payload for user profile registration.
 */
public class UserProfileRegisterRequest {
  private String authUserId;
  private String pseudo;
  private String email;
}
