package fr.waveme.crud.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String imageUrl;
    private Long userId;
    private Integer upVote;
    private Integer downVote;
    private String description;
    private Set<Long> commentIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
