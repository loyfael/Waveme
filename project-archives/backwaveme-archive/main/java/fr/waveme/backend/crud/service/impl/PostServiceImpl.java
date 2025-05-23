package fr.waveme.backend.crud.service.impl;

import fr.waveme.backend.crud.dto.PostDto;
import fr.waveme.backend.crud.models.Post;
import fr.waveme.backend.crud.service.PostService;

import java.util.List;

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
