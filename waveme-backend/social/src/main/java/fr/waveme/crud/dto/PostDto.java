package fr.waveme.crud.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDto {
    private Long id;
    private String imageUrl;
    private String description;
    private String shortUrl;
    private String userId;
    private Integer upVote;
    private Integer downVote;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
