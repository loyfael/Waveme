package fr.waveme.backend.social.crud.exception;

import org.springframework.http.HttpStatus;

/**
 * UserNotFoundException is thrown when a user is not found in the system.
 * It extends RuntimeException, allowing it to be thrown without being declared in method signatures.
 */
public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(HttpStatus notFound, String message) {
    super(message);
  }
}
