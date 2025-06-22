package fr.waveme.backend.social.crud.dto.pub;

import fr.waveme.backend.social.crud.dto.pub.post.PostMetadataDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPageDto {
  private List<PostMetadataDto> content;
  private int currentPage;
  private int totalPages;
  private long totalElements;
}