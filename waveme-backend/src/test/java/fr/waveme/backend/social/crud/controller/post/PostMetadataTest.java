package fr.waveme.backend.social.crud.controller.post;

import fr.waveme.backend.social.crud.controller.PostController;
import fr.waveme.backend.social.crud.dto.pub.UserSimpleInfoDto;
import fr.waveme.backend.social.crud.dto.pub.post.PostPublicDto;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.models.UserProfile;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.social.crud.repository.UserProfileRepository;
import fr.waveme.backend.social.crud.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostMetadataTest {
    private PostService postService;
    private PostController controller;

    @BeforeEach
    void setUp() {
        postService = mock(PostService.class);
        controller = new PostController(postService);
    }

    @Test
    void getPostMetadata_shouldReturnDto() {
        // Arrange
        Long postId = 3L;
        UserSimpleInfoDto userDto = new UserSimpleInfoDto("u1", "Test", "img.png");
        PostPublicDto postDto = new PostPublicDto(
                postId,
                "Description",
                "/api/image/get/mongo123",
                Instant.now(),
                10,
                5,
                5,
                userDto,
                new ArrayList<>()
        );

        // Configurer le service mocké pour retourner une réponse valide
        when(postService.getPostMetadata(postId)).thenReturn(ResponseEntity.ok(postDto));

        // Act
        ResponseEntity<?> res = controller.getPostMetadata(postId);

        // Assert
        assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    void getPostMetadata_shouldThrowIfPostNotFound() {
        // Arrange
        Long postId = 404L;

        // Configurer le service mocké pour lancer l'exception attendue
        when(postService.getPostMetadata(postId)).thenThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> controller.getPostMetadata(postId));
    }

    @Test
    void getPostMetadata_shouldThrowIfUserNotFound() {
        // Arrange
        Long postId = 1L;

        // Configurer le service mocké pour lancer l'exception attendue
        when(postService.getPostMetadata(postId)).thenThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User profile not found"));

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> controller.getPostMetadata(postId));
    }
}
