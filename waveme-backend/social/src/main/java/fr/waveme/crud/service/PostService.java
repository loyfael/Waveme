package fr.waveme.crud.service;

import fr.waveme.crud.models.Post;

import java.util.List;

public interface PostService {
    Post create(Post post);
    List<Post> getAll();
    Post getById(Long id);
    List<Post> getByUserId(String userId);
}
