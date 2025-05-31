package fr.waveme.backend.social.crud.dto;

import java.time.LocalDateTime;

public class UserSocialPublicDto {
  public String id;
  public String pseudo;
  public int totalUpVote;
  public int totalPosts;
  public LocalDateTime createdAt;
  public LocalDateTime updatedAt;

  public UserSocialPublicDto (String id, String pseudo, int totalUpVote, int totalPosts, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.pseudo = pseudo;
    this.totalUpVote = totalUpVote;
    this.totalPosts = totalPosts;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
