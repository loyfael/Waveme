package fr.waveme.crud.mapper;

import fr.waveme.crud.dto.ReplyDto;
import fr.waveme.crud.models.Reply;

public class ReplyMapper {
    public static ReplyDto toDto(Reply reply) {
        ReplyDto dto = new ReplyDto();
        dto.setId(reply.getId());
        dto.setDescription(reply.getDescription());
        dto.setUserId(reply.getUserId());
        dto.setCreatedAt(reply.getCreatedAt());
        dto.setUpdatedAt(reply.getUpdatedAt());
        dto.setUserId(reply.getUserId());
        dto.setCommentId(reply.getCommentId());
        return dto;
    }
}
