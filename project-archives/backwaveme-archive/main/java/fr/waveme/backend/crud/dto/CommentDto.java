package fr.waveme.backend.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private Long userId;
    private Integer upVote;
    private Integer downVote;
    private String description;
    private Set<Long> replyIds;
}
