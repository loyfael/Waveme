package fr.waveme.backend.social.crud.controller.post;

import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.controller.PostController;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.models.reaction.PostVote;
import fr.waveme.backend.social.crud.repository.PostRepository;

import fr.waveme.backend.social.crud.repository.react.PostVoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class PostVoteTest {

  private final PostRepository postRepository = mock(PostRepository.class);
  private final PostVoteRepository postVoteRepository = mock(PostVoteRepository.class);
  private final JwtUtils jwtUtils = mock(JwtUtils.class);

  private final PostController postController = new PostController(
          null, // minioService
          postRepository,
          postVoteRepository,
          jwtUtils,
          null, // sequence
          null, // comment repo
          null, // reply repo
          null  // userProfile repo
  );

  @Test
  void testVotePost_shouldSaveVoteAndUpdatePost() {
    // Arrange
    Long postId = 42L;
    String jwtToken = "Bearer mock.jwt.token";
    String userId = "user123";
    Post post = new Post();
    post.setPostUniqueId(postId);
    post.setUpVote(0);
    post.setDownVote(0);

    when(jwtUtils.getSocialUserIdFromJwtToken("mock.jwt.token")).thenReturn(userId);
    when(postVoteRepository.existsByPostIdAndUserId(postId, userId)).thenReturn(false);
    when(postRepository.findByPostUniqueId(postId)).thenReturn(Optional.of(post));

    // Act
    ResponseEntity<?> response = postController.votePost(postId, true, jwtToken);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Vote enregistré", response.getBody());

    verify(postVoteRepository).save(any(PostVote.class));
    verify(postRepository).save(argThat(savedPost ->
            savedPost.getUpVote() == 1 && savedPost.getDownVote() == 0
    ));
  }

  @Test
  void testVotePost_shouldReturnForbiddenIfAlreadyVoted() {
    Long postId = 42L;
    String jwtToken = "Bearer mock.jwt.token";
    String userId = "user123";

    when(jwtUtils.getSocialUserIdFromJwtToken("mock.jwt.token")).thenReturn(userId);
    when(postVoteRepository.existsByPostIdAndUserId(postId, userId)).thenReturn(true);

    ResponseEntity<?> response = postController.votePost(postId, true, jwtToken);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertEquals("Vous avez déjà voté pour ce post.", response.getBody());

    verify(postVoteRepository, never()).save(any());
    verify(postRepository, never()).save(any());
  }
}
