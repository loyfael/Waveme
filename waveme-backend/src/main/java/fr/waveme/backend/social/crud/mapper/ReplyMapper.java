package fr.waveme.backend.social.crud.mapper;

import fr.waveme.backend.social.crud.dto.ReplyDto;
import fr.waveme.backend.social.crud.models.Reply;

/**
 * ReplyMapper is a utility class that provides methods to convert between
 * Reply and ReplyDto objects.
 * It is used to map data between the domain model and the data transfer object.
 */
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
