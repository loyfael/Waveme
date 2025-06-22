package fr.waveme.backend.social.crud.service.impl;

import fr.waveme.backend.social.crud.dto.CommentDto;
import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * CommentServiceImpl provides the implementation of the CommentService interface.
 * It contains methods to create, retrieve, update, and delete comments.
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Override
    public CommentDto createComment(CommentDto commentDto) {
        return null;
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        return null;
    }

    @Override
    public List<Comment> getComments() {
        return List.of();
    }

    @Override
    public CommentDto updateComment(Long commentId, String userId, Integer upVote, Integer downVote, String description) {
        return null;
    }

    @Override
    public CommentDto deleteComment(Long commentId, String userId) {
        return null;
    }
}
