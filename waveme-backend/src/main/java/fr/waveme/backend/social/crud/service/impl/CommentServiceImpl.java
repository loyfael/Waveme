package fr.waveme.backend.social.crud.service.impl;

import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.dto.pub.UserSimpleInfoDto;
import fr.waveme.backend.social.crud.dto.pub.comment.CommentPublicDto;
import fr.waveme.backend.social.crud.dto.pub.react.CommentVoteDetailsDto;
import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.models.reaction.CommentVote;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.social.crud.repository.UserProfileRepository;
import fr.waveme.backend.social.crud.repository.react.CommentVoteRepository;
import fr.waveme.backend.social.crud.sequence.SequenceGeneratorService;
import fr.waveme.backend.social.crud.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * CommentServiceImpl provides the implementation of the CommentService interface.
 * It contains methods to create, retrieve, update, and delete comments.
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired private final CommentRepository commentRepository;
    @Autowired private final PostRepository postRepository;
    @Autowired private final JwtUtils jwtUtils;
    @Autowired private final SequenceGeneratorService sequenceGeneratorService;
    @Autowired private final CommentVoteRepository commentVoteRepository;
    @Autowired private final UserProfileRepository userProfileRepository;

    public CommentServiceImpl(
            CommentRepository commentRepository,
            PostRepository postRepository,
            JwtUtils jwtUtils,
            SequenceGeneratorService sequenceGeneratorService,
            CommentVoteRepository commentVoteRepository,
            UserProfileRepository userProfileRepository
    ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.jwtUtils = jwtUtils;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.commentVoteRepository = commentVoteRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public Comment addCommentToPost(Long postUniqueId, String content, String token) {
        String userId = jwtUtils.getSocialUserIdFromJwtToken(token);

        Post post = postRepository.findByPostUniqueId(postUniqueId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Post not found"));

        Comment comment = new Comment();
        comment.setCommentUniqueId(sequenceGeneratorService.generateSequence("comment_sequence"));
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

        Comment comment = commentRepository.findByCommentUniqueId(commentUniqueId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Comment not found"));

        // Chercher si l'utilisateur a déjà voté sur ce commentaire
        Optional<CommentVote> existingVoteOpt = commentVoteRepository.findByCommentIdAndUserId(commentUniqueId, userId);

        if (existingVoteOpt.isPresent()) {
            CommentVote existingVote = existingVoteOpt.get();
            
            // L'utilisateur a déjà voté
            if (existingVote.isUpvote() == upvote) {
                // Même type de vote - on annule le vote
                commentVoteRepository.delete(existingVote);
                
                if (upvote) {
                    comment.setUpVote(Math.max(0, comment.getUpVote() - 1));
                } else {
                    comment.setDownVote(Math.max(0, comment.getDownVote() - 1));
                }
                
                commentRepository.save(comment);
                return ResponseEntity.ok("Vote annulé");
            } else {
                // Type de vote différent - on change le vote
                existingVote.setUpvote(upvote);
                commentVoteRepository.save(existingVote);
                
                if (upvote) {
                    // Changement vers upvote
                    comment.setUpVote(comment.getUpVote() + 1);
                    comment.setDownVote(Math.max(0, comment.getDownVote() - 1));
                } else {
                    // Changement vers downvote
                    comment.setDownVote(comment.getDownVote() + 1);
                    comment.setUpVote(Math.max(0, comment.getUpVote() - 1));
                }
                
                commentRepository.save(comment);
                return ResponseEntity.ok("Vote modifié");
            }
        } else {
            // Nouveau vote
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

    @Override
    public ResponseEntity<?> getUserCommentVotes(Long commentUniqueId) {
        Comment comment = commentRepository.findByCommentUniqueId(commentUniqueId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Comment not found"));

        List<CommentVote> votes = commentVoteRepository.findAllByCommentId(commentUniqueId);

        List<UserSimpleInfoDto> upvoters = votes.stream()
                .filter(CommentVote::isUpvote)
                .map(CommentVote::getUserId)
                .map(id -> userProfileRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .map(p -> new UserSimpleInfoDto(p.getId(), p.getPseudo(), p.getProfileImg()))
                .toList();

        List<UserSimpleInfoDto> downvoters = votes.stream()
                .filter(v -> !v.isUpvote())
                .map(CommentVote::getUserId)
                .map(id -> userProfileRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .map(p -> new UserSimpleInfoDto(p.getId(), p.getPseudo(), p.getProfileImg()))
                .toList();

        return ResponseEntity.ok(new CommentVoteDetailsDto(
                comment.getUpVote(),
                comment.getDownVote(),
                upvoters,
                downvoters
        ));
    }
}
