package fr.waveme.backend.crud.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "comments")
public class Comment {
    @Id
    private Long id;

    private Long userId;
    private Integer upVote;
    private Integer downVote;
    private String description;

    private Long postId; // référence au post parent
    private List<Long> replyIds; // références aux réponses

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
