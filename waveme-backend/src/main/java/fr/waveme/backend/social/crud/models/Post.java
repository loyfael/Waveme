package fr.waveme.backend.social.crud.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Post represents a post in the social media application.
 * It contains fields for post ID, user ID, image URL, upvotes, downvotes,
 * description, short URL, a list of comment IDs, and timestamps for creation and update.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
public class Post {
    @Id
    private String id;
    private String userId; // refering to the user who created the post

    @Indexed(unique = true)
    private Long postUniqueId;

    private String imageUrl;
    private Integer upVote;
    private Integer downVote;
    private String description;
    private String shortUrl;

    private List<Long> commentIds; // refering to the comments on this post

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
