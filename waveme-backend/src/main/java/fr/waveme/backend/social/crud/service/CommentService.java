package fr.waveme.backend.social.crud.service;

import fr.waveme.backend.social.crud.dto.CommentDto;
import fr.waveme.backend.social.crud.dto.pub.comment.CommentPublicDto;
import fr.waveme.backend.social.crud.models.Comment;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * CommentService provides methods to perform CRUD operations on Comment entities.
 * It contains methods to create, retrieve, update, and delete comments.
 */
public interface CommentService {
    Comment addCommentToPost(Long postUniqueId, String content, String token);
    ResponseEntity<?> voteComment(Long commentUniqueId, boolean upvote, String token);
    ResponseEntity<?> getCommentVotes(Long commentUniqueId);
    ResponseEntity<List<CommentPublicDto>> getCommentsByPostId(Long postUniqueId);
}
