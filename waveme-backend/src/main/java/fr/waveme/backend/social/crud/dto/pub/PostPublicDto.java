package fr.waveme.backend.social.crud.dto.pub;

import lombok.Getter;

import java.time.Instant;

/**
 * PostPublicDto represents a public data transfer object for posts in the social media application.
 * It contains fields for post ID, description, image URL, and creation timestamp.
 */
@Getter
public class PostPublicDto {
    private final String id;
    private final String description;
    private final String imageUrl;
    private final Instant createdAt;

    public PostPublicDto(String id, String description, String imageUrl, Instant createdAt) {
        this.id = id;
        this.description = description;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

}
