package fr.waveme.backend.social.crud.dto.pub;

import fr.waveme.backend.social.crud.dto.pub.comment.CommentSummaryDto;
import fr.waveme.backend.social.crud.dto.pub.post.PostSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * UserSocialPublicDto represents a public data transfer object for user profiles in the social media application.
 * It contains fields for user ID, pseudo, total posts, total comments, total upvotes, profile image URL,
 * and timestamps for creation and last update.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSocialPublicDto {
  public String id;
  public String pseudo;
  public int totalPosts;
  private int totalComments;
  public int totalUpVote;
  private String profileImg;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<PostSummaryDto> latestPosts;
  private List<CommentSummaryDto> latestComments;
}
