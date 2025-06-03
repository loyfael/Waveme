package fr.waveme.backend.social.crud.exception;

/**
 * CommentNotFoundException is thrown when a comment is not found in the system.
 * It extends RuntimeException, allowing it to be thrown without being declared in method signatures.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
