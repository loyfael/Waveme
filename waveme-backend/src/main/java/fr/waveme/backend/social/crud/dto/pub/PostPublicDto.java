package fr.waveme.backend.social.crud.dto.pub;

import lombok.Getter;

import java.time.Instant;

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
