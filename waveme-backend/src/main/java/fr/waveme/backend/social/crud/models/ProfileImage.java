package fr.waveme.backend.social.crud.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "profile_images")
public class ProfileImage {
  @Id
  private String id;

  private String minioObjectName;
  private String bucketName;
}
