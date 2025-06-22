package fr.waveme.backend.social.crud.service.impl;

import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.dto.pub.comment.CommentPublicDto;
import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.models.reaction.CommentVote;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.social.crud.repository.react.CommentVoteRepository;
import fr.waveme.backend.social.crud.sequence.SequenceGeneratorService;
import fr.waveme.backend.social.crud.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * CommentServiceImpl provides the implementation of the CommentService interface.
 * It contains methods to create, retrieve, update, and delete comments.
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private SequenceGeneratorService sequenceGenerator;
    @Autowired private CommentVoteRepository commentVoteRepository;

    public Comment addCommentToPost(Long postUniqueId, String content, String token) {
        String userId = jwtUtils.getSocialUserIdFromJwtToken(token);

        Post post = postRepository.findByPostUniqueId(postUniqueId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Post not found"));

        Comment comment = new Comment();
        comment.setCommentUniqueId(sequenceGenerator.generateSequence("comment_sequence"));
        comment.setUserId(userId);
        comment.setPostId(post.getPostUniqueId());
        comment.setDescription(content);
        comment.setUpVote(0);
        comment.setDownVote(0);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public ResponseEntity<String> voteComment(Long commentUniqueId, boolean upvote, String token) {
        String userId = jwtUtils.getSocialUserIdFromJwtToken(token);

        if (commentVoteRepository.existsByCommentIdAndUserId(commentUniqueId, userId)) {
            return ResponseEntity.status(FORBIDDEN).body("Vous avez déjà voté pour ce commentaire.");
        }

        Comment comment = commentRepository.findByCommentUniqueId(commentUniqueId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Comment not found"));

        CommentVote vote = new CommentVote();
        vote.setCommentId(commentUniqueId);
        vote.setUserId(userId);
        vote.setUpvote(upvote);
        commentVoteRepository.save(vote);

        if (upvote) comment.setUpVote(comment.getUpVote() + 1);
        else comment.setDownVote(comment.getDownVote() + 1);

        commentRepository.save(comment);
        return ResponseEntity.ok("Vote enregistré");
    }

    public ResponseEntity<?> getCommentVotes(Long commentUniqueId) {
        Comment comment = commentRepository.findByCommentUniqueId(commentUniqueId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Comment not found"));

        return ResponseEntity.ok(Map.of(
                "upVote", comment.getUpVote(),
                "downVote", comment.getDownVote()
        ));
    }

    public ResponseEntity<List<CommentPublicDto>> getCommentsByPostId(Long postUniqueId) {
        List<CommentPublicDto> comments = commentRepository.findAllByPostId(postUniqueId).stream()
                .map(comment -> new CommentPublicDto(
                        comment.getId(),
                        comment.getDescription(),
                        comment.getUserId(),
                        comment.getUpVote(),
                        comment.getDownVote(),
                        comment.getCreatedAt() != null
                                ? comment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()
                                : Instant.EPOCH,
                        List.of()
                ))
                .toList();

        return ResponseEntity.ok(comments);
    }
}
