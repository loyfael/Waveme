package fr.waveme.backend.social.crud.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * UserProfileDto represents a data transfer object for user profiles in the social media application.
 * It contains fields for user ID, authentication ID, pseudo, email, profile image URL,
 * total upvotes, total posts, total comments, and timestamps for creation and last update.
 */
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
