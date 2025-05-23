package fr.waveme.crud.mapper;

import fr.waveme.crud.dto.CommentDto;
import fr.waveme.crud.models.Comment;

public class CommentMapper {
    public static CommentDto commentToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setDescription(comment.getDescription());
        dto.setUserId(comment.getUserId());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        dto.setUserId(comment.getUserId());
        dto.setDownVote(comment.getDownVote());
        dto.setUpVote(comment.getUpVote());
        dto.setPostId(comment.getPostId());
        return dto;
    }
}
