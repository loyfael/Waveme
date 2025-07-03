package fr.waveme.backend.social.crud.service.impl;

import fr.waveme.backend.social.crud.dto.pub.UserSimpleInfoDto;
import fr.waveme.backend.social.crud.dto.pub.post.PostMetadataDto;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.models.UserProfile;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.social.crud.repository.UserProfileRepository;
import fr.waveme.backend.social.crud.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class FeedServiceImpl implements FeedService {

  private final PostRepository postRepository;
  private final UserProfileRepository userProfileRepository;

  @Autowired
  public FeedServiceImpl(PostRepository postRepository, UserProfileRepository userProfileRepository) {
    this.postRepository = postRepository;
    this.userProfileRepository = userProfileRepository;
  }

  @Override
  public Page<PostMetadataDto> getFeedMetadata(int page) {
    int forcedSize = 10;

    Page<Post> postPage = postRepository.findAll(
            PageRequest.of(page, forcedSize, Sort.by(Sort.Direction.DESC, "createdAt"))
    );

    return postPage.map(post -> {
      // Get user profile information
      UserSimpleInfoDto userInfo = null;
      if (post.getUserId() != null) {
        Optional<UserProfile> userProfile = userProfileRepository.findById(post.getUserId());
        if (userProfile.isPresent()) {
          UserProfile profile = userProfile.get();
          userInfo = new UserSimpleInfoDto(
                  profile.getId(),
                  profile.getPseudo(),
                  profile.getProfileImg()
          );
        }
      }
      
      // If no user profile found, create a default one
      if (userInfo == null) {
        userInfo = new UserSimpleInfoDto(
                "unknown",
                "Utilisateur inconnu",
                null
        );
      }

      return new PostMetadataDto(
              post.getId(),
              post.getPostUniqueId(),
              "/api/image/get/" + post.getId(),
              post.getDescription(),
              post.getUpVote(),
              post.getDownVote(),
              post.getCreatedAt() != null
                      ? post.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()
                      : Instant.EPOCH,
              userInfo
      );
    });
  }
}
