package fr.waveme.crud.service.impl;

import fr.waveme.crud.models.Post;
import fr.waveme.crud.repository.PostRepository;
import fr.waveme.crud.service.PostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository repository;

    public PostServiceImpl(PostRepository repository) {
        this.repository = repository;
    }

    @Override
    public Post create(Post post) {
        return repository.save(post);
    }

    @Override
    public List<Post> getAll() {
        return repository.findAll();
    }

    @Override
    public Post getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public List<Post> getByUserId(String userId) {
        return repository.findByUserId(userId);
    }
}