package fr.waveme.backend.auth.crud.dto.pub;

import java.time.LocalDateTime;

public class UserPublicDto {
  public Long id;
  public String pseudo;
  public String profileImg;
  public int totalUpvotes;
  public int totalPosts;
  public LocalDateTime createdAt;
  public LocalDateTime updatedAt;

  public UserPublicDto(Long id, String pseudo, String profileImg, int totalUpvotes, int totalPosts,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.pseudo = pseudo;
    this.profileImg = profileImg;
    this.totalUpvotes = totalUpvotes;
    this.totalPosts = totalPosts;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
