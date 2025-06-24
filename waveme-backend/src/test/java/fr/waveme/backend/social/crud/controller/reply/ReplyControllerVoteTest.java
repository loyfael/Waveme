package fr.waveme.backend.social.crud.controller.reply;

import fr.waveme.backend.social.crud.controller.ReplyController;
import fr.waveme.backend.social.crud.models.Reply;
import fr.waveme.backend.social.crud.service.ReplyService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

public class ReplyControllerVoteTest {
    @Mock
    private ReplyService replyService;

    private ReplyController controller;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        controller = new ReplyController(replyService);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void vote_shouldSucceed() {
        // Arrange
        Long replyId = 101L;
        boolean isUpvote = true;
        String token = "Bearer token";

        // Configurez le service mocké pour retourner une réponse réussie
        ResponseEntity<String> mockResponse = ResponseEntity.ok("Vote enregistré");
        doReturn(mockResponse).when(replyService).voteReply(eq(replyId), eq(isUpvote), eq(token));

        // Act
        ResponseEntity<?> response = controller.voteReply(replyId, isUpvote, token);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Vote enregistré", response.getBody());
    }

    @Test
    void vote_shouldRejectIfAlreadyVoted() {
        // Arrange
        Long replyId = 101L;
        boolean isUpvote = true;
        String token = "Bearer token";

        // Configurez le service mocké pour retourner une réponse d'interdiction
        ResponseEntity<String> mockResponse = ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Vous avez déjà voté pour cette réponse.");
        doReturn(mockResponse).when(replyService).voteReply(eq(replyId), eq(isUpvote), eq(token));

        // Act
        ResponseEntity<?> response = controller.voteReply(replyId, isUpvote, token);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Vous avez déjà voté pour cette réponse.", response.getBody());
    }

    @Test
    void vote_shouldFailIfReplyNotFound() {
        // Arrange
        Long replyId = 999L;
        boolean isUpvote = true;
        String token = "Bearer token";

        // Configurez le service pour lancer une exception quand la réponse n'est pas trouvée
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Réponse non trouvée"))
                .when(replyService).voteReply(eq(replyId), eq(isUpvote), eq(token));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            controller.voteReply(replyId, isUpvote, token);
        });
    }
}
