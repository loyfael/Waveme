package com.alpineblue.waveme_backend.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String userId;
    private Integer upVote;
    private Integer downVote;
    private String description;
}
