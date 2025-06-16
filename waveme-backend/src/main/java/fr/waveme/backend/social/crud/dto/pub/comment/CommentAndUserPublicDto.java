package fr.waveme.backend.social.crud.dto.pub.comment;

import fr.waveme.backend.social.crud.dto.pub.UserSimpleInfoDto;
import fr.waveme.backend.social.crud.dto.pub.reply.ReplyAndUserPublicDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
public class CommentAndUserPublicDto {
  private final String id;
  private final String content;
  private final int upVote;
  private final int downVote;
  private final String author;
  private final Instant createdAt;
  private final UserSimpleInfoDto userInfo;
  private final List<ReplyAndUserPublicDto> replies;
}
