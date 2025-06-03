package fr.waveme.backend.social.crud.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * UserProfile represents a user's profile in the social media application.
 * It contains fields for user ID, authentication user ID, pseudo, email,
 * profile image, total upvotes, total posts, total comments, and timestamps
 * for creation and update.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class UserProfile {
  @Id
  private String id; // id social interne (String, ou Long si tu préfères)

  private Long authUserId; // id de l’utilisateur côté Auth
  private String pseudo;
  private String email;
  private String profileImg;
  private int totalUpVotes;
  private int totalPosts;
  private int totalComments;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;
}
