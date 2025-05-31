package fr.waveme.backend.social.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
