package fr.waveme.backend.auth.crud.dto.pub;

import java.time.LocalDateTime;

public class UserPublicDto {
  public Long id;
  public String pseudo;
  public int totalUpvotes;
  public int totalPosts;
  public LocalDateTime createdAt;
  public LocalDateTime updatedAt;

  public UserPublicDto(Long id, String pseudo, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.pseudo = pseudo;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
