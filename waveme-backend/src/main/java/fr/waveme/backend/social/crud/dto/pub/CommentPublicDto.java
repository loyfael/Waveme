package fr.waveme.backend.social.crud.dto.pub;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
public class CommentPublicDto {
  private final String id;
  private final String content;
  private final String author;
  private final int upVote;
  private final int downVote;
  private final Instant createdAt;
  private final List<ReplyPublicDto> replies;
}
