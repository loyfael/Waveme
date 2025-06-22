package fr.waveme.backend.social.crud.controller.reply;

import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.controller.ReplyController;
import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.models.Reply;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.ReplyRepository;
import fr.waveme.backend.social.crud.repository.react.ReplyVoteRepository;
import fr.waveme.backend.social.crud.sequence.SequenceGeneratorService;
import fr.waveme.backend.social.crud.service.ReplyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReplyControllerAddTest {
    ReplyRepository replyRepository = mock(ReplyRepository.class);
    CommentRepository commentRepository = mock(CommentRepository.class);
    JwtUtils jwtUtils = mock(JwtUtils.class);
    SequenceGeneratorService sequence = mock(SequenceGeneratorService.class);
    ReplyVoteRepository voteRepository = mock(ReplyVoteRepository.class);
    ReplyService replyService = mock(ReplyService.class);

    ReplyController controller = new ReplyController(replyService);

    @Test
    void addReply_shouldSucceed() {
        Comment comment = new Comment();
        comment.setCommentUniqueId(88L);

        when(jwtUtils.getSocialUserIdFromJwtToken("token")).thenReturn("user123");
        when(commentRepository.findByCommentUniqueId(88L)).thenReturn(Optional.of(comment));
        when(sequence.generateSequence("reply_sequence")).thenReturn(55L);
        when(replyRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Reply reply = controller.addReplyToComment(88L, "Test reply", "Bearer token", "1.2.3.4").getBody();

      Assertions.assertNotNull(reply);
      assertEquals("Test reply", reply.getDescription());
        assertEquals("user123", reply.getUserId());
        assertEquals(88L, reply.getCommentId());
        assertEquals(55L, reply.getReplyUniqueId());
    }

    @Test
    void addReply_shouldFailIfCommentNotFound() {
        when(commentRepository.findByCommentUniqueId(404L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            controller.addReplyToComment(404L, "fail", "Bearer token", null);
        });
    }
}
