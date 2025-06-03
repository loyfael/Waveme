package fr.waveme.backend.social.crud.service;

import fr.waveme.backend.social.crud.dto.ReplyDto;
import fr.waveme.backend.social.crud.models.Reply;

import java.util.List;

/**
 * ReplyService provides methods to perform CRUD operations on Reply entities.
 * It contains methods to create, retrieve, update, and delete replies.
 */
public interface ReplyService {
    ReplyDto createReply(ReplyDto replyDto);
    ReplyDto getReById(Long ReplyId);
    List<Reply> getReplys();
    ReplyDto updateReply(Long ReplyId, String userId, Integer upVote, Integer downVote, String description);
    ReplyDto deleteReply(Long ReplyId, String userId);
}
