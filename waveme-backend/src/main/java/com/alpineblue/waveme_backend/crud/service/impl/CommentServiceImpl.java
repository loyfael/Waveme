package com.alpineblue.waveme_backend.crud.service.impl;

import com.alpineblue.waveme_backend.crud.dto.CommentDto;
import com.alpineblue.waveme_backend.crud.entity.Comment;
import com.alpineblue.waveme_backend.crud.service.CommentService;

import java.util.List;

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
