package fr.waveme.backend.auth.payload.response;

import lombok.*;

/**
 * MessageResponse is a class that represents a simple response containing a message.
 * It is typically used to send feedback to the client, such as success or error messages.
 */
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private String message;
}
