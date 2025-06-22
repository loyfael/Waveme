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
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class PostVoteTest {


  private final PostService postService = mock(PostService.class);
  private final PostController controller = new PostController(postService);

  @Test
  void testVotePost_shouldSaveVoteAndUpdatePost() {
    ResponseEntity<String> mockResponse = ResponseEntity.ok("Vote enregistré");

    when(postService.votePost(123L, true, "Bearer token"))
            .thenReturn(mockResponse);

    ResponseEntity<String> response = controller.votePost(123L, true, "Bearer token");

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Vote enregistré", response.getBody());
  }

  @Test
  void testVotePost_shouldReturnForbiddenIfAlreadyVoted() {
    ResponseEntity<String> mockResponse = ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body("Vous avez déjà voté pour ce post.");

    when(postService.votePost(123L, false, "Bearer token"))
            .thenReturn(mockResponse);

    ResponseEntity<String> response = controller.votePost(123L, false, "Bearer token");

    assertNotNull(response);
    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertEquals("Vous avez déjà voté pour ce post.", response.getBody());
  }
}
