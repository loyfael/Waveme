package fr.waveme.backend.auth.payload.request;

import lombok.Getter;
import lombok.Setter;

/**
 * UserProfileRegisterRequest is a class that represents the request payload for user profile registration.
 * It contains fields for the authenticated user's ID, pseudo, and email.
 * This class is used to validate the input data when a user registers their profile.
 */
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
