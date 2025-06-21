package fr.waveme.backend.social.crud.dto.pub.report;

import fr.waveme.backend.social.crud.models.enumerator.EReportReason;
import lombok.Data;

@Data
public class ReportCreateDto {
  private EReportReason reason;
  private String description;
}
