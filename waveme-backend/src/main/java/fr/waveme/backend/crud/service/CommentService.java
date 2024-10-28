package fr.waveme.backend.crud.service;

import fr.waveme.backend.crud.dto.CommentDto;
import fr.waveme.backend.crud.dto.PostDto;
import fr.waveme.backend.crud.entity.Comment;
import fr.waveme.backend.crud.entity.Post;

import java.util.List;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto);
    CommentDto getCommentById(Long commentId);
    List<Comment> getComments();
    CommentDto updateComment(Long commentId, String userId, Integer upVote, Integer downVote, String description);
    CommentDto deleteComment(Long commentId, String userId);
}
