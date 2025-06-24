package fr.waveme.backend.social.crud.controller.reply;

import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.controller.ReplyController;
import fr.waveme.backend.social.crud.models.Reply;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.ReplyRepository;
import fr.waveme.backend.social.crud.repository.react.ReplyVoteRepository;
import fr.waveme.backend.social.crud.sequence.SequenceGeneratorService;
import fr.waveme.backend.social.crud.service.ReplyService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

public class ReplyControllerAddTest {
    @Mock
    private ReplyRepository replyRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private SequenceGeneratorService sequence;

    @Mock
    private ReplyVoteRepository voteRepository;

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
    void addReply_shouldSucceed() {
        // Arrange
        Long commentId = 88L;
        String content = "Test reply";
        String token = "Bearer token";
        String ipAddress = "1.2.3.4";

        Reply mockReply = new Reply();
        mockReply.setDescription(content);
        mockReply.setUserId("user123");
        mockReply.setCommentId(commentId);
        mockReply.setReplyUniqueId(55L);

        // Configurez le comportement du service mocké
        doReturn(ResponseEntity.ok(mockReply)).when(replyService)
                .addReplyToComment(eq(commentId), eq(content), eq(token), eq(ipAddress));

        // Act
        ResponseEntity<?> response = controller.addReplyToComment(commentId, content, token, ipAddress);

        // Assert
        Assertions.assertNotNull(response);
        Reply reply = (Reply) response.getBody();
        Assertions.assertNotNull(reply);
        assertEquals("Test reply", reply.getDescription());
        assertEquals("user123", reply.getUserId());
        assertEquals(88L, reply.getCommentId());
        assertEquals(55L, reply.getReplyUniqueId());
    }

    @Test
    void addReply_shouldFailIfCommentNotFound() {
        // Arrange
        Long commentId = 404L;
        String content = "fail";
        String token = "Bearer token";
        String ipAddress = null;

        // Configurez le service pour lancer une exception quand le commentaire n'est pas trouvé
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"))
                .when(replyService).addReplyToComment(eq(commentId), eq(content), eq(token), eq(ipAddress));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            controller.addReplyToComment(commentId, content, token, ipAddress);
        });
    }
}
