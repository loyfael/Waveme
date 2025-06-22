package fr.waveme.backend.social.crud.service.impl;

import fr.waveme.backend.social.crud.dto.ReplyDto;
import fr.waveme.backend.social.crud.models.Reply;
import fr.waveme.backend.social.crud.service.ReplyService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ReplyServiceImpl provides the implementation of the ReplyService interface.
 * It contains methods to create, retrieve, update, and delete replies.
 */
@Service
public class ReplyServiceImpl implements ReplyService {
    @Override
    public ReplyDto createReply(ReplyDto replyDto) {
        return null;
    }

    @Override
    public ReplyDto getReById(Long ReplyId) {
        return null;
    }

    @Override
    public List<Reply> getReplys() {
        return List.of();
    }

    @Override
    public ReplyDto updateReply(Long ReplyId, String userId, Integer upVote, Integer downVote, String description) {
        return null;
    }

    @Override
    public ReplyDto deleteReply(Long ReplyId, String userId) {
        return null;
    }
}
