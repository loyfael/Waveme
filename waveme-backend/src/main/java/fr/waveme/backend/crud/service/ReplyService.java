package fr.waveme.backend.crud.service;

import fr.waveme.backend.crud.dto.ReplyDto;
import fr.waveme.backend.crud.dto.ReplyDto;
import fr.waveme.backend.crud.models.Reply;

import java.util.List;

public interface ReplyService {
    ReplyDto createReply(ReplyDto replyDto);
    ReplyDto getReById(Long ReplyId);
    List<Reply> getReplys();
    ReplyDto updateReply(Long ReplyId, String userId, Integer upVote, Integer downVote, String description);
    ReplyDto deleteReply(Long ReplyId, String userId);
}
