package fr.waveme.backend.social.crud.service;

import fr.waveme.backend.social.crud.dto.CommentDto;
import fr.waveme.backend.social.crud.models.Comment;

import java.util.List;

/**
 * CommentService provides methods to perform CRUD operations on Comment entities.
 * It contains methods to create, retrieve, update, and delete comments.
 */
public interface CommentService {
    CommentDto createComment(CommentDto commentDto);
    CommentDto getCommentById(Long commentId);
    List<Comment> getComments();
    CommentDto updateComment(Long commentId, String userId, Integer upVote, Integer downVote, String description);
    CommentDto deleteComment(Long commentId, String userId);
}
