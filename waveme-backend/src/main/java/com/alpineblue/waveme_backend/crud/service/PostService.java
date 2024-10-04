package com.alpineblue.waveme_backend.crud.service;

import com.alpineblue.waveme_backend.crud.dto.PostDto;
import com.alpineblue.waveme_backend.crud.dto.UserDto;
import com.alpineblue.waveme_backend.crud.entity.Post;
import com.alpineblue.waveme_backend.crud.entity.User;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);
    PostDto getPostById(Long postId);
    List<Post> getPosts();
    PostDto updatePost(Long postId, String userId, String imageUrl, Integer upVote, Integer downVote);
    PostDto deletePost(Long postId, String userId);
}
