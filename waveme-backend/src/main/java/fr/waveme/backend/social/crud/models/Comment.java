package fr.waveme.backend.social.crud.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Comment represents a comment in the social media application.
 * It contains fields for comment ID, user ID, upvotes, downvotes, description,
 * a reference to the parent post, and a list of reply IDs.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;
    private String userId;

    @Indexed(unique = true)
    private Long commentUniqueId;

    private Integer upVote;
    private Integer downVote;
    private String description;

    private Long postId;
    private List<Long> replyIds;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
