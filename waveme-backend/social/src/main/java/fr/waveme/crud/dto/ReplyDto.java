package fr.waveme.crud.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReplyDto {
    private Long id;
    private Long commentId;
    private String userId;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
