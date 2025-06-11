package fr.waveme.backend.social.crud.dto.pub;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class CommentSummaryDto {
  private final String id;
  private final String content;
  private final int voteSum;
  private final int upVote;
  private final int downVote;
  private final Instant createdAt;
}
