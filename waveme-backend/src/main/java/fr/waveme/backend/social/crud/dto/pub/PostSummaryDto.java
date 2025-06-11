package fr.waveme.backend.social.crud.dto.pub;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class PostSummaryDto {
  private final String id;
  private final Long postUniqueId;
  private final String imageUrl;
  private final String description;
  private final int upVote;
  private final int downVote;
  private final int voteSum;
  private final Instant createdAt;
}
