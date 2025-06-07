package fr.waveme.backend.social.crud.models.reaction;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("post_votes")
public class PostVote {
    @Id
    private String id;

    @Indexed
    private Long postId;

    @Indexed
    private String userId;

    private boolean upvote;
}

