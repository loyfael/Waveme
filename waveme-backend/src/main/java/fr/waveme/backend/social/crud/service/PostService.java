package fr.waveme.backend.social.crud.service;

import fr.waveme.backend.social.crud.dto.PostDto;
import fr.waveme.backend.social.crud.models.Post;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);
    PostDto getPostById(Long postId);
    List<Post> getPosts();
    PostDto updatePost(Long postId, String userId, String imageUrl, Integer upVote, Integer downVote);
    PostDto deletePost(Long postId, String userId);
}
