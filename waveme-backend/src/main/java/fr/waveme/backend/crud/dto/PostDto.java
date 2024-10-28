package fr.waveme.backend.crud.dto;

import fr.waveme.backend.crud.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String userId;
    private String imageUrl;
    private Integer upVote;
    private Integer downVote;
    private Set<Comment> comment;
}
