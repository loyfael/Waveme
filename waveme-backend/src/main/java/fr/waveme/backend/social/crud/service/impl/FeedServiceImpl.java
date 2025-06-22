package fr.waveme.backend.social.crud.service.impl;

import fr.waveme.backend.social.crud.dto.pub.post.PostMetadataDto;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.social.crud.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;

@Service
public class FeedServiceImpl implements FeedService {

  private final PostRepository postRepository;

  @Autowired
  public FeedServiceImpl(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Override
  public Page<PostMetadataDto> getFeedMetadata(int page) {
    int forcedSize = 10;

    Page<Post> postPage = postRepository.findAll(
            PageRequest.of(page, forcedSize, Sort.by(Sort.Direction.DESC, "createdAt"))
    );

    return postPage.map(post -> new PostMetadataDto(
            post.getId(),
            post.getPostUniqueId(),
            "/api/image/get/" + post.getId(),
            post.getDescription(),
            post.getUpVote(),
            post.getDownVote(),
            post.getCreatedAt() != null
                    ? post.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()
                    : Instant.EPOCH
    ));
  }
}
