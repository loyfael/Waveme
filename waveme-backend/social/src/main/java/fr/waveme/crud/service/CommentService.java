package fr.waveme.crud.service;

import fr.waveme.crud.models.Comment;

import java.util.List;

public interface CommentService {
    Comment create(Comment comment);
    List<Comment> getByPostId(Long postId);
}
