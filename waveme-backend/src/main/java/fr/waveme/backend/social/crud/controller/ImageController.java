package fr.waveme.backend.social.crud.controller;

import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.models.ProfileImage;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.social.crud.repository.ProfileImageRepository;
import fr.waveme.backend.social.crud.service.MinioService;
import fr.waveme.backend.utils.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/image") // pour que la route complète soit /api/image/get/{id}
public class ImageController {

  private final Logger logger = LoggerFactory.getLogger(ImageController.class);
  private final PostRepository postRepository;
  private final ProfileImageRepository profileImageRepository; // ⬅️ à injecter
  private final MinioService minioService;

  public ImageController(PostRepository postRepository, ProfileImageRepository profileImageRepository, MinioService minioService) {
    this.postRepository = postRepository;
    this.profileImageRepository = profileImageRepository; // ⬅️ stocker l'injection
    this.minioService = minioService;
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<byte[]> downloadImage(
          @PathVariable("id") String id,
          @RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress
  ) {
    ipAddress = ipAddress != null ? ipAddress : "unknown";

    String bucketName;
    String objectName = id;

    // ✅ D'abord chercher dans les posts
    Optional<Post> postOpt = postRepository.findById(id);
    if (postOpt.isPresent()) {
      try {
        String imageUrl = postOpt.get().getImageUrl();
        String[] parts = imageUrl.split("/");
        bucketName = parts[3];
        objectName = parts[4].split("\\?")[0]; // si jamais tu ajoutes des query params plus tard
      } catch (Exception e) {
        throw new ResponseStatusException(BAD_REQUEST, "Malformed image URL in post");
      }
    } else {
      // ✅ Sinon on suppose que c'est une image de profil
      ProfileImage image = profileImageRepository.findById(id)
              .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Image not found"));
      bucketName = image.getBucketName();
      objectName = image.getMinioObjectName();
    }

    try (InputStream inputStream = minioService.downloadImage(bucketName, objectName)) {
      byte[] imageBytes = inputStream.readAllBytes();

      return ResponseEntity.ok()
              .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + objectName + "\"")
              .contentType(getMediaTypeFromExtension(objectName))
              .body(imageBytes);

    } catch (Exception e) {
      logger.error("Error during image download: {}", e.getMessage(), e);
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
              .body(("Error during image download: " + e.getMessage()).getBytes());
    }
  }

  private MediaType getMediaTypeFromExtension(String fileName) {
    String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    return switch (ext) {
      case "png" -> MediaType.IMAGE_PNG;
      case "webp" -> MediaType.valueOf("image/webp");
      case "jpeg", "jpg" -> MediaType.IMAGE_JPEG;
      default -> MediaType.APPLICATION_OCTET_STREAM;
    };
  }
}
