package fr.waveme.backend.social.crud.controller.reply;

import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.controller.ReplyController;
import fr.waveme.backend.social.crud.models.Reply;
import fr.waveme.backend.social.crud.models.reaction.ReplyVote;
import fr.waveme.backend.social.crud.repository.ReplyRepository;
import fr.waveme.backend.social.crud.repository.react.ReplyVoteRepository;
import fr.waveme.backend.social.crud.service.ReplyService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ReplyControllerVoteTest {
    ReplyRepository replyRepository = mock(ReplyRepository.class);
    JwtUtils jwtUtils = mock(JwtUtils.class);
    ReplyVoteRepository voteRepository = mock(ReplyVoteRepository.class);
    ReplyService replyService = mock(ReplyService.class);

    ReplyController controller = new ReplyController(replyService);

    @Test
    void vote_shouldSucceed() {
        Reply reply = new Reply();
        reply.setReplyUniqueId(101L);
        reply.setUpVote(0);
        reply.setDownVote(0);

        when(jwtUtils.getSocialUserIdFromJwtToken("token")).thenReturn("user1");
        when(replyRepository.findByReplyUniqueId(101L)).thenReturn(Optional.of(reply));
        when(voteRepository.existsByReplyIdAndUserId(101L, "user1")).thenReturn(false);

        ResponseEntity<?> response = controller.voteReply(101L, true, "Bearer token");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Vote enregistré", response.getBody());
        verify(replyRepository).save(any(Reply.class));
        verify(voteRepository).save(any(ReplyVote.class));
    }

    @Test
    void vote_shouldRejectIfAlreadyVoted() {
        when(jwtUtils.getSocialUserIdFromJwtToken("token")).thenReturn("user1");
        when(voteRepository.existsByReplyIdAndUserId(101L, "user1")).thenReturn(true);

        ResponseEntity<?> response = controller.voteReply(101L, true, "Bearer token");

        assertEquals(403, response.getStatusCode().value());
        assertEquals("Vous avez déjà voté pour cette réponse.", response.getBody());
    }

    @Test
    void vote_shouldFailIfReplyNotFound() {
        when(jwtUtils.getSocialUserIdFromJwtToken("token")).thenReturn("user1");
        when(voteRepository.existsByReplyIdAndUserId(101L, "user1")).thenReturn(false);
        when(replyRepository.findByReplyUniqueId(101L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            controller.voteReply(101L, true, "Bearer token");
        });
    }
}
