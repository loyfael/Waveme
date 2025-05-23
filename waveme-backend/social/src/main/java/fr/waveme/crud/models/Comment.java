package fr.waveme.crud.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "comment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    private Long id;

    private Long postId;
    private String userId;

    private Integer upVote;
    private Integer downVote;

    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
