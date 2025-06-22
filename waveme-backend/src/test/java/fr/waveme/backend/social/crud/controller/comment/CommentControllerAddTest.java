package fr.waveme.backend.social.crud.controller.comment;

import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.controller.CommentController;
import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.social.crud.repository.react.CommentVoteRepository;
import fr.waveme.backend.social.crud.sequence.SequenceGeneratorService;
import fr.waveme.backend.social.crud.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommentControllerAddTest {
    CommentRepository commentRepository = mock(CommentRepository.class);
    PostRepository postRepository = mock(PostRepository.class);
    JwtUtils jwtUtils = mock(JwtUtils.class);
    SequenceGeneratorService sequence = mock(SequenceGeneratorService.class);
    CommentService commentService = mock(CommentService.class);
    CommentController controller = new CommentController(commentService);

    @Test
    void addComment_shouldSucceed() {
        Post post = new Post();
        post.setPostUniqueId(42L);

        when(jwtUtils.getSocialUserIdFromJwtToken("token")).thenReturn("user1");
        when(postRepository.findByPostUniqueId(42L)).thenReturn(Optional.of(post));
        when(sequence.generateSequence("comment_sequence")).thenReturn(100L);
        when(commentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Comment comment = controller.addCommentToPost(42L, "Hello", "Bearer token");

        assertEquals("Hello", comment.getDescription());
        assertEquals(100L, comment.getCommentUniqueId());
        assertEquals("user1", comment.getUserId());
    }

    @Test
    void addComment_shouldFailIfPostNotFound() {
        when(postRepository.findByPostUniqueId(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            controller.addCommentToPost(99L, "test", "Bearer token");
        });
    }
}
