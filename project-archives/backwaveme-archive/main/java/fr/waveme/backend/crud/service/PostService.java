package fr.waveme.backend.crud.service;

import fr.waveme.backend.crud.dto.PostDto;
import fr.waveme.backend.crud.models.Post;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);
    PostDto getPostById(Long postId);
    List<Post> getPosts();
    PostDto updatePost(Long postId, String userId, String imageUrl, Integer upVote, Integer downVote);
    PostDto deletePost(Long postId, String userId);
}
