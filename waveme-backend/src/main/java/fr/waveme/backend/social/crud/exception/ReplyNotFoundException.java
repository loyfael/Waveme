package fr.waveme.backend.social.crud.exception;

public class ReplyNotFoundException extends RuntimeException {
  public ReplyNotFoundException(String message) {
    super(message);
  }
}
