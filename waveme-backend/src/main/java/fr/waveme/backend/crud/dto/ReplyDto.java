package fr.waveme.backend.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {
    private Long id;
    private Long userId;
    private Integer upVote;
    private Integer downVote;
    private String description;
}
