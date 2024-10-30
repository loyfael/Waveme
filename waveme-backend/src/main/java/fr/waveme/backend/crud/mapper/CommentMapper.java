package fr.waveme.backend.crud.mapper;


import fr.waveme.backend.crud.dto.CommentDto;
import fr.waveme.backend.crud.models.Comment;

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
