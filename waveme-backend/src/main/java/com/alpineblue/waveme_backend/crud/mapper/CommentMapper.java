package com.alpineblue.waveme_backend.crud.mapper;


import com.alpineblue.waveme_backend.crud.dto.CommentDto;
import com.alpineblue.waveme_backend.crud.entity.Comment;

public class CommentMapper {
    public static CommentDto mapToCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getUserId(),
                comment.getUpVote(),
                comment.getDownVote(),
                comment.getDescription()
        );
    }

    public static Comment mapToComment(CommentDto commentDto) {
        Comment comment = new Comment();

        comment.setId(commentDto.getId());
        comment.setUserId(commentDto.getUserId());
        comment.setUpVote(commentDto.getUpVote());
        comment.setDownVote(commentDto.getDownVote());
        comment.setDescription(commentDto.getDescription());

        return comment;
    }
}
