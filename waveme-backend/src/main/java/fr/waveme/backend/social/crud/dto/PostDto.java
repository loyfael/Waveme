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
public class PostDto {
    private String id;
    private String imageUrl;
    private Integer upVote;
    private Integer downVote;
    private List<Long> commentIds;
}
