package fr.waveme.backend.social.crud.dto;

import fr.waveme.backend.social.crud.models.enumerator.EReportReason;
import fr.waveme.backend.social.crud.models.enumerator.EReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
  private String id;
  private String reporterId;
  private String commentId;
  private String replyId;
  private EReportReason reason;
  private String description;
  private EReportStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
