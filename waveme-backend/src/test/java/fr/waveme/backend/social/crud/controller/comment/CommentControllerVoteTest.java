package fr.waveme.backend.social.crud.controller.comment;

import fr.waveme.backend.social.crud.controller.CommentController;
import fr.waveme.backend.social.crud.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentControllerVoteTest {
    CommentService commentService = mock(CommentService.class);
    CommentController controller = new CommentController(commentService);


    @Test
    void vote_shouldRegisterVote() {
        CommentService commentService = mock(CommentService.class);
        CommentController controller = spy(new CommentController(commentService));

        doReturn(ResponseEntity.ok("Vote enregistré"))
                .when(commentService).voteComment(anyLong(), anyBoolean(), anyString());

        ResponseEntity<?> response = controller.voteComment(1L, true, "Bearer token");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Vote enregistré", response.getBody());
    }

    @Test
    void vote_shouldRejectIfAlreadyVoted() {
        CommentService commentService = mock(CommentService.class);
        CommentController controller = spy(new CommentController(commentService));

        doReturn(ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Vous avez déjà voté pour ce commentaire."))
                .when(commentService).voteComment(anyLong(), anyBoolean(), anyString());

        ResponseEntity<?> response = controller.voteComment(1L, true, "Bearer token");

        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Vous avez déjà voté pour ce commentaire.", response.getBody());
    }

    @Test
    void vote_shouldFailIfCommentNotFound() {
        CommentService commentService = mock(CommentService.class);
        CommentController controller = new CommentController(commentService);

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"))
                .when(commentService).voteComment(anyLong(), anyBoolean(), anyString());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                controller.voteComment(1L, true, "Bearer token"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
