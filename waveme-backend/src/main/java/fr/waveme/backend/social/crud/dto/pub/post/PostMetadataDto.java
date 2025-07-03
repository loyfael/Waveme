package fr.waveme.backend.social.crud.dto.pub.post;

import fr.waveme.backend.social.crud.dto.pub.UserSimpleInfoDto;
import lombok.Getter;

import java.time.Instant;

@Getter
public class PostMetadataDto {
  private final Long postUniqueId;
  private final String imageUrl;
  private final String description;
  private final int upVote;
  private final int downVote;
  private final int voteSum;
  private final Instant createdAt;
  private final UserSimpleInfoDto user;

  public PostMetadataDto(String id, Long postUniqueId, String imageUrl, String description, int upVote, int downVote, Instant createdAt, UserSimpleInfoDto user) {
    this.postUniqueId = postUniqueId;
    this.imageUrl = imageUrl;
    this.description = description;
    this.upVote = upVote;
    this.downVote = downVote;
    this.voteSum = upVote - downVote;
    this.createdAt = createdAt;
    this.user = user;
  }
}
