package fr.waveme.backend.social.crud.exception;

/**
 * CommentNotFoundException is thrown when a comment is not found in the system.
 * It extends RuntimeException, allowing it to be thrown without being declared in method signatures.
 */
public class CommentNotFoundException extends RuntimeException {
  public CommentNotFoundException(String message) {
    super(message);
  }
}
