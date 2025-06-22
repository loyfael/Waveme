package fr.waveme.backend.social.crud.controller.post;

import fr.waveme.backend.social.crud.controller.PostController;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.models.UserProfile;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.social.crud.repository.UserProfileRepository;
import fr.waveme.backend.social.crud.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostMetadataTest {
    private final PostService postService = mock(PostService.class);
    private final UserProfileRepository userProfileRepository = mock(UserProfileRepository.class);
    private final CommentRepository commentRepository = mock(CommentRepository.class);
    private final PostRepository postRepository = mock(PostRepository.class);

    private final PostController controller = new PostController(postService);

    @Test
    void getPostMetadata_shouldReturnDto() {
        Long postId = 3L;
        Post post = new Post();
        post.setId("mongo123");
        post.setPostUniqueId(postId);
        post.setUserId("u1");
        post.setCreatedAt(LocalDateTime.now());
        post.setUpVote(0);
        post.setDownVote(0);

        UserProfile profile = new UserProfile();
        profile.setId("u1");
        profile.setPseudo("Test");
        profile.setProfileImg("img.png");

        when(postRepository.findByPostUniqueId(postId)).thenReturn(Optional.of(post));
        when(userProfileRepository.findById("u1")).thenReturn(Optional.of(profile));
        when(commentRepository.findAllByPostId(postId)).thenReturn(java.util.List.of());

        ResponseEntity<?> res = controller.getPostMetadata(postId);
        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    void getPostMetadata_shouldThrowIfPostNotFound() {
        when(postRepository.findByPostUniqueId(404L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> controller.getPostMetadata(404L));
    }

    @Test
    void getPostMetadata_shouldThrowIfUserNotFound() {
        Post post = new Post();
        post.setPostUniqueId(1L);
        post.setUserId("badUser");

        when(postRepository.findByPostUniqueId(1L)).thenReturn(Optional.of(post));
        when(userProfileRepository.findById("badUser")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> controller.getPostMetadata(1L));
    }
}
