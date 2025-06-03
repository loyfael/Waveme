package fr.waveme.backend.social.crud.mapper;


import fr.waveme.backend.social.crud.dto.CommentDto;
import fr.waveme.backend.social.crud.models.Comment;

/**
 * CommentMapper is a utility class that provides methods to convert between
 * Comment and CommentDto objects.
 * It is used to map data between the domain model and the data transfer object.
 */
public class CommentMapper {
    public static CommentDto mapToCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getUserId(),
                comment.getUpVote(),
                comment.getDownVote(),
                comment.getDescription(),
                comment.getReplyIds()
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
