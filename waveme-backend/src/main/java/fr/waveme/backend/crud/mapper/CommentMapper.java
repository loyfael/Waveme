package fr.waveme.backend.crud.mapper;


import fr.waveme.backend.crud.dto.CommentDto;
import fr.waveme.backend.crud.models.Comment;
import fr.waveme.backend.crud.models.Reply;

import java.util.stream.Collectors;

public class CommentMapper {
    public static CommentDto mapToCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getUserId(),
                comment.getUpVote(),
                comment.getDownVote(),
                comment.getDescription(),
                comment.getReply().stream()
                        .map(Reply::getId)
                        .collect(Collectors.toSet())
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
