package fr.waveme.backend.social.crud.dto.pub;

import lombok.Getter;

@Getter
public class UserSimpleInfoDto {
  private final String id;
  private final String pseudo;
  private final String profileImg;

  public UserSimpleInfoDto(String id, String pseudo, String profileImg) {
    this.id = id;
    this.pseudo = pseudo;
    this.profileImg = profileImg;
  }
}
