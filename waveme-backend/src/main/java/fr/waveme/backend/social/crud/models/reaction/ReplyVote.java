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
@Document("reply_votes")
public class ReplyVote {
  @Id
  private String id;

  @Indexed
  private Long replyId;

  @Indexed
  private String userId;

  private boolean upvote;
}
