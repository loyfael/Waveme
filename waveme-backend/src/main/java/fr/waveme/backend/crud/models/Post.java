package fr.waveme.backend.crud.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
public class Post {
    @Id
    private Long id;

    private String imageUrl;
    private Integer upVote;
    private Integer downVote;
    private String description;
    private String shortUrl;

    private Long userId; // refering to the user who created the post

    private List<Long> commentIds; // refering to the comments on this post

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
