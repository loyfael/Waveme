package com.alpineblue.waveme_backend.crud.mapper;

import com.alpineblue.waveme_backend.crud.dto.PostDto;
import com.alpineblue.waveme_backend.crud.entity.Post;

public class PostMapper {
    public static PostDto mapToPostDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getUserId(),
                post.getImageUrl(),
                post.getUpVote(),
                post.getDownVote(),
                post.getComment()
        );
    }

    public static Post mapToPost(PostDto postDto) {
        Post post = new Post();

        post.setId(postDto.getId());
        post.setUserId(postDto.getUserId());
        post.setImageUrl(postDto.getImageUrl());
        post.setUpVote(postDto.getUpVote());
        post.setDownVote(postDto.getDownVote());
        post.setComment(postDto.getComment());

        return post;
    }
}
