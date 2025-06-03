package fr.waveme.backend.social.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * PostRequest represents the request payload for creating or updating a post.
 * It contains fields for the image URL, description, upvote count, and downvote count.
 * The upvote and downvote counts are initialized to 0 by default.
 */
@Getter
@Setter
public class PostRequest {
    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    private String description;

    @NotNull(message = "Upvote count cannot be null")
    private Integer upVote = 0; // Valeur par défaut

    @NotNull(message = "Downvote count cannot be null")
    private Integer downVote = 0; // Valeur par défaut
}
