package fr.waveme.backend.social.crud.mapper;

import fr.waveme.backend.social.crud.dto.UserProfileDto;
import fr.waveme.backend.social.crud.models.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {

  public UserProfileDto toDto(UserProfile entity) {
    if (entity == null) return null;
    return UserProfileDto.builder()
            .id(entity.getId())
            .authId(entity.getAuthUserId())
            .pseudo(entity.getPseudo())
            .email(entity.getEmail())
            .profileImg(entity.getProfileImg())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
  }

  public UserProfile toEntity(UserProfileDto dto) {
    if (dto == null) return null;
    return UserProfile.builder()
            .id(dto.getId())
            .authUserId(dto.getAuthId())
            .pseudo(dto.getPseudo())
            .email(dto.getEmail())
            .profileImg(dto.getProfileImg())
            .createdAt(dto.getCreatedAt())
            .updatedAt(dto.getUpdatedAt())
            .build();
  }
}

