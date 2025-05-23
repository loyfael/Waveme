package fr.waveme.crud.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    private Long id;
    private String imageUrl;
    private Integer upVote;
    private Integer downVote;
    private String description;
    private String shortUrl;
    private Long userId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
