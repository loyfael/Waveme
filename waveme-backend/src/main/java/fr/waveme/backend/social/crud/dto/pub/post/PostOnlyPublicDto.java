package fr.waveme.backend.social.crud.dto.pub.post;

import fr.waveme.backend.social.crud.dto.pub.UserSimpleInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

/**
 * PostPublicDto represents a public data transfer object for posts in the social media application.
 * It contains fields for post ID, description, image URL, and creation timestamp.
 */
@Getter
@AllArgsConstructor
public class PostOnlyPublicDto {
    private final Long postUniqueId;
    private final String description;
    private final String imageUrl;
    private final Instant createdAt;
    private final int upVote;
    private final int downVote;
    private final int voteSum;
    private final UserSimpleInfoDto user;
}
