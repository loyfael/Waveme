package fr.waveme.backend.crud.dto.pub;

import lombok.Getter;

import java.time.Instant;

@Getter
public class PostPublicDto {
    private final Long id;
    private final String description;
    private final String imageUrl;
    private final Instant createdAt;

    public PostPublicDto(Long id, String description, String imageUrl, Instant createdAt) {
        this.id = id;
        this.description = description;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

}
