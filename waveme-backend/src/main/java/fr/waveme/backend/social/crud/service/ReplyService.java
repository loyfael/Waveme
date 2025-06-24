package fr.waveme.backend.social.crud.service;

import fr.waveme.backend.social.crud.dto.ReplyDto;
import fr.waveme.backend.social.crud.dto.pub.reply.ReplyPublicDto;
import fr.waveme.backend.social.crud.models.Reply;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * ReplyService provides methods to perform CRUD operations on Reply entities.
 * It contains methods to create, retrieve, update, and delete replies.
 */
public interface ReplyService {
    ResponseEntity<Reply> addReplyToComment(Long commentUniqueId, String content, String authorizationHeader, String ipAddress);

    ResponseEntity<String> voteReply(Long replyUniqueId, boolean upvote, String authorizationHeader);

    ResponseEntity<?> getReplyVotes(Long replyUniqueId);

    ResponseEntity<List<ReplyPublicDto>> getRepliesByCommentId(Long commentUniqueId);

    ResponseEntity<?> getUserReplyVotes(Long replyUniqueId);
}