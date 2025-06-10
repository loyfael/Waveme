package fr.waveme.backend.social.crud.dto.pub;

import lombok.Getter;

@Getter
public class UserInPostPublicDto {
  private final String id;
  private final String pseudo;
  private final String profileImg;

  public UserInPostPublicDto(String id, String pseudo, String profileImg) {
    this.id = id;
    this.pseudo = pseudo;
    this.profileImg = profileImg;
  }
}
