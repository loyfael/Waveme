package fr.waveme.backend.controller;

import fr.waveme.backend.crud.dto.UserDto;
import fr.waveme.backend.crud.dto.pub.PostPublicDto;
import fr.waveme.backend.crud.dto.pub.UserPublicDto;
import fr.waveme.backend.crud.models.User;
import fr.waveme.backend.crud.repository.CommentRepository;
import fr.waveme.backend.crud.repository.PostRepository;
import fr.waveme.backend.crud.repository.ReplyRepository;
import fr.waveme.backend.crud.repository.UserRepository;
import fr.waveme.backend.security.jwt.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final ReplyRepository replyRepository;
  private final JwtUtils jwtUtils;

    public UserController(
        UserRepository userRepository,
        PostRepository postRepository,
        CommentRepository commentRepository,
        ReplyRepository replyRepository,
        JwtUtils jwtUtils

    ) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.replyRepository = replyRepository;
        this.jwtUtils = jwtUtils;
    }

  @GetMapping("me")
  public ResponseEntity<UserPublicDto> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {

    String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
    Long userId = jwtUtils.getUserIdFromJwtToken(token);

    if (userId == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token: user ID not found");
    }

    User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    int postUpvotes = postRepository.findByUser(user).stream()
            .mapToInt(p -> p.getUpVote() != null ? p.getUpVote() : 0).sum();

    int commentUpvotes = commentRepository.findByUserId(user.getId()).stream()
            .mapToInt(c -> c.getUpVote() != null ? c.getUpVote() : 0).sum();

    int replyUpvotes = replyRepository.findByUserId(user.getId().toString()).stream()
            .mapToInt(r -> r.getUpVote() != null ? r.getUpVote() : 0).sum();

    int totalUpvotes = postUpvotes + commentUpvotes + replyUpvotes;
    int totalPosts = postRepository.findByUser(user).size();

    UserPublicDto dto = new UserPublicDto(
            user.getId(),
            user.getPseudo(),
            user.getProfileImg(),
            totalUpvotes,
            totalPosts,
            user.getCreatedAt(),
            user.getUpdatedAt()
    );

    return ResponseEntity.ok(dto);
  }


  @GetMapping("{id}/posts")
  public ResponseEntity<List<PostPublicDto>> getUserPosts(@PathVariable Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    List<PostPublicDto> posts = postRepository.findByUser(user).stream()
            .map(post -> new PostPublicDto(
                    post.getId(),
                    post.getDescription(),
                    post.getImageUrl(),
                    post.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant()
            ))
            .toList();

    return ResponseEntity.ok(posts);
  }


}
