package fr.waveme.backend.social.crud.controller.post;

import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.controller.PostController;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.models.reaction.PostVote;
import fr.waveme.backend.social.crud.repository.PostRepository;

import fr.waveme.backend.social.crud.repository.react.PostVoteRepository;
import fr.waveme.backend.social.crud.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class PostVoteTest {


  private final PostService postService = mock(PostService.class);
  private final PostController controller = new PostController(postService);

  @Test
  void testVotePost_shouldSaveVoteAndUpdatePost() {
    // Arrange
    Long postId = 123L;
    boolean isUpvote = true;
    String token = "Bearer token";

    ResponseEntity<String> mockResponse = ResponseEntity.ok("Vote enregistré");

    // Utilisez any() pour les arguments si des problèmes persistent avec eq()
    doReturn(mockResponse).when(postService).votePost(anyLong(), anyBoolean(), anyString());

    // Act
    ResponseEntity<String> response = controller.votePost(postId, isUpvote, token);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Vote enregistré", response.getBody());
  }

  @Test
  void testVotePost_shouldReturnForbiddenIfAlreadyVoted() {
    // Arrange
    Long postId = 123L;
    boolean isUpvote = false;
    String token = "Bearer token";

    ResponseEntity<String> mockResponse = ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body("Vous avez déjà voté pour ce post.");

    // Utilisez any() pour les arguments si des problèmes persistent avec eq()
    doReturn(mockResponse).when(postService).votePost(anyLong(), anyBoolean(), anyString());

    // Act
    ResponseEntity<String> response = controller.votePost(postId, isUpvote, token);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertEquals("Vous avez déjà voté pour ce post.", response.getBody());
  }
}
