package fr.waveme.backend.social.crud.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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

    private String imageUrl;
    private Integer upVote;
    private Integer downVote;
    private String description;
    private String shortUrl;

    private List<Long> commentIds; // refering to the comments on this post

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
