package fr.waveme.backend.social.crud.service.impl;

import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.dto.pub.UserSimpleInfoDto;
import fr.waveme.backend.social.crud.dto.pub.comment.CommentAndUserPublicDto;
import fr.waveme.backend.social.crud.dto.pub.post.PostPublicDto;
import fr.waveme.backend.social.crud.dto.pub.react.PostVoteDetailsDto;
import fr.waveme.backend.social.crud.dto.pub.reply.ReplyAndUserPublicDto;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.models.UserProfile;
import fr.waveme.backend.social.crud.models.reaction.PostVote;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.social.crud.repository.ReplyRepository;
import fr.waveme.backend.social.crud.repository.UserProfileRepository;
import fr.waveme.backend.social.crud.repository.react.PostVoteRepository;
import fr.waveme.backend.social.crud.sequence.SequenceGeneratorService;
import fr.waveme.backend.social.crud.service.MinioService;
import fr.waveme.backend.social.crud.service.PostService;
import fr.waveme.backend.utils.UrlShorter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
 * PostServiceImpl provides the implementation of the PostService interface.
 * It contains methods to create, retrieve, update, and delete posts.
 */
@Service
public class PostServiceImpl implements PostService {
    @Autowired private MinioService minioService;
    @Autowired private final PostRepository postRepository;
    @Autowired private final PostVoteRepository postVoteRepository;
    @Autowired private final JwtUtils jwtUtils;
    @Autowired private final SequenceGeneratorService sequenceGeneratorService;
    @Autowired private CommentRepository commentRepository;
    @Autowired private ReplyRepository replyRepository;
    @Autowired private UserProfileRepository userProfileRepository;

    public PostServiceImpl(
            PostRepository postRepository,
            JwtUtils jwtUtils,
            SequenceGeneratorService sequenceGeneratorService,
            PostVoteRepository postVoteRepository
    ) {
        this.postRepository = postRepository;
        this.jwtUtils = jwtUtils;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.postVoteRepository = postVoteRepository;
    }
    @Override
    public ResponseEntity<String> uploadPostImage(MultipartFile file, String bucketName, String description, String token) {
        String userId = jwtUtils.getSocialUserIdFromJwtToken(token);

        Post post = new Post();
        post.setUserId(userId);
        post.setPostUniqueId(sequenceGeneratorService.generateSequence("post_sequence"));
        post.setDescription(description);
        post.setUpVote(0);
        post.setDownVote(0);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        String url = minioService.uploadImage(file, bucketName, post);
        post.setImageUrl(url);
        postRepository.save(post);

        UrlShorter urlShorter = new UrlShorter();
        String shortUrl = urlShorter.generateShortUrl(url);

        return ResponseEntity.ok(new UrlShorter().generateShortUrl(url));
    }

    public ResponseEntity<PostPublicDto> getPostMetadata(Long postUniqueId) {
        Post post = postRepository.findByPostUniqueId(postUniqueId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Post not found"));

        UserProfile userProfile = userProfileRepository.findById(post.getUserId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User profile not found"));

        UserSimpleInfoDto userDto = new UserSimpleInfoDto(
                userProfile.getId(), userProfile.getPseudo(), userProfile.getProfileImg()
        );

        List<CommentAndUserPublicDto> comments = commentRepository.findAllByPostId(postUniqueId).stream().map(
                comment -> {
            UserProfile commentAuthor = userProfileRepository.findById(comment.getUserId())
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User profile not found"));
            UserSimpleInfoDto commentUserDto = new UserSimpleInfoDto(commentAuthor.getId(), commentAuthor.getPseudo(), commentAuthor.getProfileImg());

            List<ReplyAndUserPublicDto> replies = replyRepository.findAllByCommentId(comment.getCommentUniqueId()).stream().map(reply -> {
                UserProfile replyAuthor = userProfileRepository.findById(reply.getUserId())
                        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User profile not found"));
                UserSimpleInfoDto replyUserDto = new UserSimpleInfoDto(replyAuthor.getId(), replyAuthor.getPseudo(), replyAuthor.getProfileImg());
                return new ReplyAndUserPublicDto(reply.getId(), reply.getReplyUniqueId(), reply.getDescription(), reply.getUpVote(), reply.getDownVote(), reply.getUserId(),
                        reply.getCreatedAt() != null ? reply.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant() : Instant.EPOCH, replyUserDto);
            }).toList();

            return new CommentAndUserPublicDto(comment.getId(), comment.getCommentUniqueId(), comment.getDescription(), comment.getUpVote(), comment.getDownVote(), comment.getUserId(),
                    comment.getCreatedAt() != null ? comment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant() : Instant.EPOCH, commentUserDto, replies);
        }).toList();

        return ResponseEntity.ok(new PostPublicDto(
                post.getPostUniqueId(),
                post.getDescription(),
                "/api/image/get/" + post.getId(),
                post.getCreatedAt() != null ? post.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant() : Instant.EPOCH,
                post.getUpVote(),
                post.getDownVote(),
                post.getUpVote() - post.getDownVote(),
                userDto,
                comments
        ));
    }

    public ResponseEntity<String> votePost(Long postUniqueId, boolean upvote, String token) {
        String userId = jwtUtils.getSocialUserIdFromJwtToken(token);

        Post post = postRepository.findByPostUniqueId(postUniqueId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Post not found"));

        // Chercher si l'utilisateur a déjà voté sur ce post
        Optional<PostVote> existingVoteOpt = postVoteRepository.findByPostIdAndUserId(postUniqueId, userId);

        if (existingVoteOpt.isPresent()) {
            PostVote existingVote = existingVoteOpt.get();
            
            // L'utilisateur a déjà voté
            if (existingVote.isUpvote() == upvote) {
                // Même type de vote - on annule le vote
                postVoteRepository.delete(existingVote);
                
                if (upvote) {
                    post.setUpVote(Math.max(0, post.getUpVote() - 1));
                } else {
                    post.setDownVote(Math.max(0, post.getDownVote() - 1));
                }
                
                postRepository.save(post);
                return ResponseEntity.ok("Vote annulé");
            } else {
                // Type de vote différent - on change le vote
                existingVote.setUpvote(upvote);
                postVoteRepository.save(existingVote);
                
                if (upvote) {
                    // Changement vers upvote
                    post.setUpVote(post.getUpVote() + 1);
                    post.setDownVote(Math.max(0, post.getDownVote() - 1));
                } else {
                    // Changement vers downvote
                    post.setDownVote(post.getDownVote() + 1);
                    post.setUpVote(Math.max(0, post.getUpVote() - 1));
                }
                
                postRepository.save(post);
                return ResponseEntity.ok("Vote modifié");
            }
        } else {
            // Nouveau vote
            PostVote vote = new PostVote();
            vote.setPostId(postUniqueId);
            vote.setUserId(userId);
            vote.setUpvote(upvote);
            postVoteRepository.save(vote);

            if (upvote) post.setUpVote(post.getUpVote() + 1);
            else post.setDownVote(post.getDownVote() + 1);

            postRepository.save(post);
            return ResponseEntity.ok("Vote enregistré");
        }
    }

    public ResponseEntity<?> getPostVotes(Long postUniqueId) {
        Post post = postRepository.findByPostUniqueId(postUniqueId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Post not found"));

        return ResponseEntity.ok(Map.of(
                "upVote", post.getUpVote(),
                "downVote", post.getDownVote()
        ));
    }

    @Override
    public ResponseEntity<?> getUserPostVotes(Long postUniqueId) {
        Post post = postRepository.findByPostUniqueId(postUniqueId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Post not found"));

        List<PostVote> votes = postVoteRepository.findAllByPostId(postUniqueId);

        List<UserSimpleInfoDto> upvoters = votes.stream()
                .filter(PostVote::isUpvote)
                .map(PostVote::getUserId)
                .map(userId -> userProfileRepository.findById(userId)
                        .map(u -> new UserSimpleInfoDto(u.getId(), u.getPseudo(), u.getProfileImg()))
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();

        List<UserSimpleInfoDto> downvoters = votes.stream()
                .filter(v -> !v.isUpvote())
                .map(PostVote::getUserId)
                .map(userId -> userProfileRepository.findById(userId)
                        .map(u -> new UserSimpleInfoDto(u.getId(), u.getPseudo(), u.getProfileImg()))
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();

        PostVoteDetailsDto response = new PostVoteDetailsDto(
                post.getUpVote(),
                post.getDownVote(),
                upvoters,
                downvoters
        );

        return ResponseEntity.ok(response);
    }
}
