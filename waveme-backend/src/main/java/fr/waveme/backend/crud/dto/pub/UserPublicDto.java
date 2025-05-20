package fr.waveme.backend.crud.dto.pub;

import java.time.LocalDateTime;

public class UserPublicDto {
  public Long id;
  public String pseudo;
  public String profileImg;
  public LocalDateTime createdAt;
  public LocalDateTime updatedAt;
  public int totalUpvotes;

  public UserPublicDto(Long id, String pseudo, String profileImg,
                       LocalDateTime createdAt, LocalDateTime updatedAt, int totalUpvotes) {
    this.id = id;
    this.pseudo = pseudo;
    this.profileImg = profileImg;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.totalUpvotes = totalUpvotes;
  }
}
