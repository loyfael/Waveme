package fr.waveme.backend.social.crud.models.reaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("comment_votes")
public class CommentVote {
  @Id
  private String id;

  @Indexed
  private Long commentId;

  @Indexed
  private String userId;

  private boolean upvote;
}
