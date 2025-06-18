package fr.waveme.backend.social.crud.controller.post;

import fr.waveme.backend.social.crud.controller.PostController;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostVoteCountTest {

    private final PostRepository postRepository = mock(PostRepository.class);

    private final PostController controller = new PostController(
            null, postRepository, null, null,
            null, null, null, null
    );

    @Test
    void getPostVotes_shouldReturnCorrectCount() {
        Post post = new Post();
        post.setPostUniqueId(5L);
        post.setUpVote(12);
        post.setDownVote(3);

        when(postRepository.findByPostUniqueId(5L)).thenReturn(Optional.of(post));

        ResponseEntity<?> res = controller.getPostVotes(5L);
        Map<?, ?> body = (Map<?, ?>) res.getBody();

        Assertions.assertNotNull(body);
        assertEquals(12, body.get("upVote"));
        assertEquals(3, body.get("downVote"));
    }

    @Test
    void getPostVotes_shouldThrowIfNotFound() {
        when(postRepository.findByPostUniqueId(99L)).thenReturn(Optional.empty());

        assertThrows(
                org.springframework.web.server.ResponseStatusException.class,
                () -> controller.getPostVotes(99L)
        );
    }
}
