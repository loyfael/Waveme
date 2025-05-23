package fr.waveme.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {
    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    private String description;

    @NotNull(message = "Upvote count cannot be null")
    private Integer upVote = 0;

    @NotNull(message = "Downvote count cannot be null")
    private Integer downVote = 0;
}
