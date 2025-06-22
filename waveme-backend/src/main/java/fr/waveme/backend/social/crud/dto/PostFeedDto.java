package fr.waveme.backend.social.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostFeedDto {
  private Long postId;
  private String title;
  private String imageUrl;
  private LocalDateTime createdAt;
}
