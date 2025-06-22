package fr.waveme.backend.social.crud.controller;

import fr.waveme.backend.social.crud.dto.pub.PostPageDto;
import fr.waveme.backend.social.crud.dto.pub.post.PostMetadataDto;
import fr.waveme.backend.social.crud.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feed")
public class FeedController {
  private final FeedService feedService;

  @Autowired
  public FeedController(FeedService feedService) {
    this.feedService = feedService;
  }

  @GetMapping
  public ResponseEntity<PostPageDto> getFeed(
          @RequestParam(defaultValue = "0") int page
  ) {
    Page<PostMetadataDto> postPage = feedService.getFeedMetadata(page);

    PostPageDto dto = new PostPageDto(
            postPage.getContent(),
            postPage.getTotalPages(),
            postPage.getNumber(),
            postPage.getTotalElements()
    );

    return ResponseEntity.ok(dto);
  }
}
