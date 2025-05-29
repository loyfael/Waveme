package fr.waveme.backend.crud.mapper;

import fr.waveme.backend.crud.dto.PostDto;
import fr.waveme.backend.crud.models.Comment;
import fr.waveme.backend.crud.models.Post;

import java.util.stream.Collectors;

public class PostMapper {
    public static PostDto mapToPostDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getImageUrl(),
                post.getUpVote(),
                post.getDownVote(),
                post.getCommentIds()
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
