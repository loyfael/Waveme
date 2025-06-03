package fr.waveme.backend.social.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * CommentDto represents a data transfer object for comments in the social media application.
 * It contains fields for comment ID, user ID, upvotes, downvotes, description, and a list of reply IDs.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private String id;
    private String userId;
    private Integer upVote;
    private Integer downVote;
    private String description;
    private List<Long> replyIds;
}
