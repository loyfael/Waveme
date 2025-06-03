package fr.waveme.backend.social.crud.service.impl;

import fr.waveme.backend.social.crud.dto.PostDto;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.service.PostService;

import java.util.List;

/**
 * PostServiceImpl provides the implementation of the PostService interface.
 * It contains methods to create, retrieve, update, and delete posts.
 */
public class PostServiceImpl implements PostService {
    @Override
    public PostDto createPost(PostDto postDto) {
        return null;
    }

    @Override
    public PostDto getPostById(Long postId) {
        return null;
    }

    @Override
    public List<Post> getPosts() {
        return List.of();
    }

    @Override
    public PostDto updatePost(Long postId, String userId, String imageUrl, Integer upVote, Integer downVote) {
        return null;
    }

    @Override
    public PostDto deletePost(Long postId, String userId) {
        return null;
    }
}
