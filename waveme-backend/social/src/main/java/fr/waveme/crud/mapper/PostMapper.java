package fr.waveme.crud.mapper;

import fr.waveme.crud.dto.PostDto;
import fr.waveme.crud.models.Post;

public class PostMapper {
    public static PostDto mapToPostDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setDescription(post.getDescription());
        dto.setImageUrl(post.getImageUrl());
        dto.setUpVote(post.getUpVote());
        dto.setDownVote(post.getDownVote());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }
}