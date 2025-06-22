package fr.waveme.backend.social.crud.service;

import fr.waveme.backend.social.crud.dto.pub.post.PostMetadataDto;
import org.springframework.data.domain.Page;

public interface FeedService {
  Page<PostMetadataDto> getFeedMetadata(int page);
}
