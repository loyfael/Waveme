package fr.waveme.crud.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private Long postId;
    private String userId;
    private Integer upVote;
    private Integer downVote;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
