package fr.waveme.backend.social.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
