package fr.waveme.backend.social.crud.service;

import fr.waveme.backend.social.crud.dto.PostFeedDto;
import org.springframework.data.domain.Page;

public interface FeedService {
  Page<PostFeedDto> getLatestPosts(int page, int size);
}
