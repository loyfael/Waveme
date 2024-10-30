package fr.waveme.backend.crud.mapper;

import fr.waveme.backend.crud.dto.PostDto;
import fr.waveme.backend.crud.entity.Comment;
import fr.waveme.backend.crud.entity.Post;

import java.util.stream.Collectors;

public class PostMapper {
    public static PostDto mapToPostDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getUser().getId(),
                post.getImageUrl(),
                post.getUpVote(),
                post.getDownVote(),
                post.getComments().stream()
                        .map(Comment::getId)
                        .collect(Collectors.toSet())
        );
    }

    public static Post mapToPost(PostDto postDto) {
        Post post = new Post();

        post.setId(postDto.getId());
        post.setImageUrl(postDto.getImageUrl());
        post.setUpVote(postDto.getUpVote());
        post.setDownVote(postDto.getDownVote());
        return post;
    }
}
