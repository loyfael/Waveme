package fr.waveme.backend.social.crud.exception;

public class PostNotFoundException extends RuntimeException {
  public PostNotFoundException(String message) {
    super(message);
  }
}
