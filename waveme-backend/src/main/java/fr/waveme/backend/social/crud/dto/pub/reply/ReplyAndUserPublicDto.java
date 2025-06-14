package fr.waveme.backend.social.crud.dto.pub.reply;

import fr.waveme.backend.social.crud.dto.pub.UserSimpleInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class ReplyAndUserPublicDto {
  private final String id;
  private final String content;
  private final int upVote;
  private final int downVote;
  private final String author;
  private final Instant createdAt;
  private final UserSimpleInfoDto userInfo;
}
