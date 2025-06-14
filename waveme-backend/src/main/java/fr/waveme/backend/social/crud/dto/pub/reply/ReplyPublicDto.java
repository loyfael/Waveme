package fr.waveme.backend.social.crud.dto.pub.reply;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class ReplyPublicDto {
  private final String id;
  private final String content;
  private final int upVote;
  private final int downVote;
  private final String author;
  private final Instant createdAt;
}
