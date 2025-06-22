package fr.waveme.backend.social.crud.dto.pub;

import fr.waveme.backend.social.crud.dto.PostFeedDto;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.social.crud.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class FeedServiceImpl implements FeedService {
  private final PostRepository postRepository;

  @Autowired
  public FeedServiceImpl(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Override
  public Page<PostFeedDto> getLatestPosts(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    Page<Post> postPage = postRepository.findAll(pageable);

    return postPage.map(post -> new PostFeedDto(
            post.getPostUniqueId(),
            post.getDescription(),
            post.getImageUrl(),
            post.getCreatedAt()
    ));
  }
}
