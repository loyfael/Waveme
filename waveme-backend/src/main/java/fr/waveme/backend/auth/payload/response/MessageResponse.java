package fr.waveme.backend.auth.payload.response;

import lombok.Getter;
import lombok.Setter;

/**
 * MessageResponse is a class that represents a simple response containing a message.
 * It is typically used to send feedback to the client, such as success or error messages.
 */
@Setter
@Getter
public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }
}
