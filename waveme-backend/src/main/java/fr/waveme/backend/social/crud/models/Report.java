package fr.waveme.backend.social.crud.models;

import fr.waveme.backend.social.crud.models.enumerator.EReportReason;
import fr.waveme.backend.social.crud.models.enumerator.EReportStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Report represents a report in the social media application.
 * It contains fields for report ID, reporter ID, references to comment and reply,
 * reason for the report, description, status of the report, and timestamps for creation and update.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "reports")
public class Report {


  @Id
  private String id;

  private Long reportUniqueId;

  private String reporterId;

  private String reportedUserId;

  @DBRef
  private Comment comment;

  @DBRef
  private Reply reply;

  @DBRef
  private Post post;

  private EReportReason reason;

  private String description;

  @Builder.Default
  private EReportStatus status = EReportStatus.PENDING;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;
}
