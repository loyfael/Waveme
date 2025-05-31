package fr.waveme.backend.bridge.service;

import fr.waveme.backend.auth.crud.dto.pub.UserPublicDto;
import fr.waveme.backend.social.crud.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BridgeService {

  private final RestTemplate restTemplate;

  @Autowired
  public BridgeService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public UserPublicDto getUserFromAuth(Long userId) {
    String url = "http://localhost/api/user/" + userId;
    UserPublicDto userPublicDto = restTemplate.getForObject(url, UserPublicDto.class);

    if (userPublicDto == null) {
      throw new UserNotFoundException("User not found with ID: " + userId);
    }

    return userPublicDto;
  }
}
