package fr.waveme.crud.service.impl;

import fr.waveme.crud.models.Comment;
import fr.waveme.crud.repository.CommentRepository;
import fr.waveme.crud.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;

    public CommentServiceImpl(CommentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Comment create(Comment comment) {
        return repository.save(comment);
    }

    @Override
    public List<Comment> getByPostId(Long postId) {
        return repository.findByPostId(postId);
    }
}
