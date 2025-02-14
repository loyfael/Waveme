package fr.waveme.backend.crud.service.impl;

import fr.waveme.backend.crud.dto.ReplyDto;
import fr.waveme.backend.crud.models.Reply;
import fr.waveme.backend.crud.service.ReplyService;

import java.util.List;

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
