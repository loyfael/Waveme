package fr.waveme.backend.social.crud.dto.pub;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
}
