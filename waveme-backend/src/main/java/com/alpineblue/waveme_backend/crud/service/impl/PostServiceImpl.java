package com.alpineblue.waveme_backend.crud.service.impl;

import com.alpineblue.waveme_backend.crud.dto.PostDto;
import com.alpineblue.waveme_backend.crud.entity.Post;
import com.alpineblue.waveme_backend.crud.service.PostService;

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
