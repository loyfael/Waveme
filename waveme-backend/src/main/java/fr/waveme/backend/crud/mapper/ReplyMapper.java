package fr.waveme.backend.crud.mapper;

import fr.waveme.backend.crud.dto.ReplyDto;
import fr.waveme.backend.crud.models.Reply;

public class ReplyMapper {
    public static ReplyDto mapToReplyDto(Reply reply) {
        return new ReplyDto(
                reply.getId(),
                reply.getUserId(),
                reply.getUpVote(),
                reply.getDownVote(),
                reply.getDescription()
        );
    }

    public static Reply mapToReply(ReplyDto replyDto) {
        Reply reply = new Reply();

        reply.setId(replyDto.getId());
        reply.setUserId(replyDto.getUserId());
        reply.setUpVote(replyDto.getUpVote());
        reply.setDownVote(replyDto.getDownVote());
        reply.setDescription(replyDto.getDescription());

        return reply;
    }
}
