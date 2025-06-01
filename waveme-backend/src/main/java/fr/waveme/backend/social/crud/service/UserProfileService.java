package fr.waveme.backend.social.crud.service;

import fr.waveme.backend.social.crud.dto.UserProfileDto;

import java.util.List;
import java.util.Optional;

public interface UserProfileService {
  UserProfileDto save(UserProfileDto dto);
  Optional<UserProfileDto> findById(String id);
  List<UserProfileDto> findAll();
  void deleteById(String id);
  Optional<UserProfileDto> findByAuthId(Long authId);
}
