package fr.waveme.backend.social.crud.controller.comment;

import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.controller.CommentController;
import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.models.reaction.CommentVote;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.react.CommentVoteRepository;
import fr.waveme.backend.social.crud.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CommentControllerVoteTest {

    CommentRepository commentRepository = mock(CommentRepository.class);
    CommentVoteRepository voteRepository = mock(CommentVoteRepository.class);
    CommentService commentService = mock(CommentService.class);
    JwtUtils jwtUtils = mock(JwtUtils.class);
    CommentController controller = new CommentController(commentService);

    @Test
    void vote_shouldRegisterVote() {
        Comment comment = new Comment();
        comment.setCommentUniqueId(1L);
        comment.setUpVote(0);
        comment.setDownVote(0);

        when(jwtUtils.getSocialUserIdFromJwtToken("token")).thenReturn("user1");
        when(commentRepository.findByCommentUniqueId(1L)).thenReturn(Optional.of(comment));
        when(voteRepository.existsByCommentIdAndUserId(1L, "user1")).thenReturn(false);

        ResponseEntity<?> response = controller.voteComment(1L, true, "Bearer token");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Vote enregistré", response.getBody());
        verify(voteRepository).save(any(CommentVote.class));
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void vote_shouldRejectIfAlreadyVoted() {
        when(jwtUtils.getSocialUserIdFromJwtToken("token")).thenReturn("user1");
        when(voteRepository.existsByCommentIdAndUserId(1L, "user1")).thenReturn(true);

        ResponseEntity<?> response = controller.voteComment(1L, true, "Bearer token");

        assertEquals(403, response.getStatusCode().value());
        assertEquals("Vous avez déjà voté pour ce commentaire.", response.getBody());
    }

    @Test
    void vote_shouldFailIfCommentNotFound() {
        when(jwtUtils.getSocialUserIdFromJwtToken("token")).thenReturn("user1");
        when(voteRepository.existsByCommentIdAndUserId(1L, "user1")).thenReturn(false);
        when(commentRepository.findByCommentUniqueId(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            controller.voteComment(1L, true, "Bearer token");
        });
    }
}
