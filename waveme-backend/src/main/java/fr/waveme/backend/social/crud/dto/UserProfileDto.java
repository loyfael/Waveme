package fr.waveme.backend.social.crud.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
  private String id;
  private Long authId;
  private String pseudo;
  private String email;
  private String profileImg;
  private int totalUpVote;
  private int totalPosts;
  private int totalComments;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
