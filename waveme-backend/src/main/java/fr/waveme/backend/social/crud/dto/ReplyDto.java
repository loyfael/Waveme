package fr.waveme.backend.social.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ReplyDto represents a data transfer object for replies in the social media application.
 * It contains fields for reply ID, user ID, upvotes, downvotes, and description.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {
    private String id;
    private String userId;
    private Integer upVote;
    private Integer downVote;
    private String description;
}
