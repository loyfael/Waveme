package fr.waveme.backend.bridge.client;

import fr.waveme.backend.auth.crud.dto.pub.UserPublicDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserApiClient {
  private final RestTemplate restTemplate;
  private final String authApiBaseUrl;

  public UserApiClient(RestTemplate restTemplate, String authApiBaseUrl) {
    this.restTemplate = restTemplate;
    this.authApiBaseUrl = authApiBaseUrl;
  }

  public UserPublicDto getUserById(String userId) {
    String url = authApiBaseUrl + "/api/users/" + userId;

    return restTemplate.getForObject(url, UserPublicDto.class);
  }
}
