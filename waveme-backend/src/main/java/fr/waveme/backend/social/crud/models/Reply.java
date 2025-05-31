package fr.waveme.backend.social.crud.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "replies")
public class Reply {
    @Id
    private String id;

    private String userId;
    private Integer upVote;
    private Integer downVote;
    private String description;

    private Long commentId; // Refering to the parent comment

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
