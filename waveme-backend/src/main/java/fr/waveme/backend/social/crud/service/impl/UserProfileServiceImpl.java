package fr.waveme.backend.social.crud.service.impl;

import fr.waveme.backend.social.crud.dto.UserProfileDto;
import fr.waveme.backend.social.crud.models.UserProfile;
import fr.waveme.backend.social.crud.repository.UserProfileRepository;
import fr.waveme.backend.social.crud.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * UserProfileServiceImpl provides the implementation of the UserProfileService interface.
 * It handles CRUD operations for user profiles, including creating, retrieving, updating, and deleting user profiles.
 */
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
  private final UserProfileRepository userProfileRepository;

  private UserProfileDto toDto(UserProfile entity) {
    if (entity == null) return null;
    return new UserProfileDto(
            entity.getId(),
            entity.getAuthUserId(),
            entity.getPseudo(),
            entity.getEmail(),
            entity.getProfileImg(),
            0, // totalUpVote
            0, // totalPosts
            0, // totalComments
            entity.getCreatedAt(),
            entity.getUpdatedAt()
    );
  }

  private UserProfile toEntity(UserProfileDto dto) {
    if (dto == null) return null;
    UserProfile entity = new UserProfile();
    entity.setId(dto.getId());
    entity.setAuthUserId(dto.getAuthId());
    entity.setPseudo(dto.getPseudo());
    entity.setEmail(dto.getEmail());
    entity.setProfileImg(dto.getProfileImg());
    entity.setCreatedAt(dto.getCreatedAt());
    entity.setUpdatedAt(dto.getUpdatedAt());
    return entity;
  }

  @Override
  public UserProfileDto save(UserProfileDto dto) {
    UserProfile entity = toEntity(dto);
    UserProfile saved = userProfileRepository.save(entity);
    return toDto(saved);
  }

  @Override
  public Optional<UserProfileDto> findById(String id) {
    return userProfileRepository.findById(id).map(this::toDto);
  }

  @Override
  public List<UserProfileDto> findAll() {
    return userProfileRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
  }

  @Override
  public void deleteById(String id) {
    userProfileRepository.deleteById(id);
  }

  @Override
  public Optional<UserProfileDto> findByAuthId(Long authId) {
    return userProfileRepository.findByAuthUserId(authId).map(this::toDto);
  }
}
