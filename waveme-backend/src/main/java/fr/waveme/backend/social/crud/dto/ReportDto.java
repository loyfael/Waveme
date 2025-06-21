package fr.waveme.backend.social.crud.dto;

import fr.waveme.backend.social.crud.models.enumerator.EReportReason;
import fr.waveme.backend.social.crud.models.enumerator.EReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ReportDto represents a data transfer object for reports in the social media application.
 * It contains fields for report ID, reporter ID, comment ID, reply ID, reason, description, status,
 * and timestamps for creation and last update.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
  private String id;
  private Long reporterId;
  private Long reportedUserId; // ID of the user being reported
  private Long postId;
  private Long commentId;
  private Long replyId;
  private EReportReason reason;
  private String description;
  private EReportStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
