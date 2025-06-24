package fr.waveme.backend.social.crud.controller.post;

import fr.waveme.backend.social.crud.controller.PostController;
import fr.waveme.backend.social.crud.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

public class PostVoteCountTest {

    private PostService postService;
    private PostController controller;

    @BeforeEach
    void setUp() {
        postService = mock(PostService.class);
        controller = new PostController(postService);
    }

    @Test
    void getPostVotes_shouldReturnCorrectCount() {
        // Arrange
        Long postId = 5L;
        Map<String, Integer> votesMap = Map.of("upVote", 12, "downVote", 3);
        ResponseEntity<?> responseEntity = ResponseEntity.ok(votesMap);

        // Configurer le comportement du service mocké
        doReturn(responseEntity).when(postService).getPostVotes(eq(postId));

        // Act
        ResponseEntity<?> res = controller.getPostVotes(postId);

        // Assert
        assertNotNull(res);
        Map<?, ?> body = (Map<?, ?>) res.getBody();
        assertNotNull(body);
        assertEquals(12, body.get("upVote"));
        assertEquals(3, body.get("downVote"));
    }

    @Test
    void getPostVotes_shouldThrowIfNotFound() {
        // Arrange
        Long postId = 99L;

        // Configurer le service pour lancer l'exception attendue
        when(postService.getPostVotes(postId)).thenThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> controller.getPostVotes(postId));
    }
}