package fr.waveme.backend.social.crud.mapper;

import fr.waveme.backend.social.crud.dto.PostDto;
import fr.waveme.backend.social.crud.models.Post;

/**
 * PostMapper is a utility class that provides methods to convert between
 * Post and PostDto objects.
 * It is used to map data between the domain model and the data transfer object.
 */
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
