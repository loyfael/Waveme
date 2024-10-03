package com.alpineblue.waveme_backend.crud.service;

import com.alpineblue.waveme_backend.crud.dto.CommentDto;
import com.alpineblue.waveme_backend.crud.dto.PostDto;
import com.alpineblue.waveme_backend.crud.entity.Comment;
import com.alpineblue.waveme_backend.crud.entity.Post;

import java.util.List;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto);
    CommentDto getCommentById(Long commentId);
    List<Comment> getComments();
    CommentDto updateComment(Long commentId, String userId, Integer upVote, Integer downVote, String description);
    CommentDto deleteComment(Long commentId, String userId);
}
