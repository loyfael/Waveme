package fr.waveme.backend.social.crud.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(HttpStatus notFound, String message) {
    super(message);
  }
}
