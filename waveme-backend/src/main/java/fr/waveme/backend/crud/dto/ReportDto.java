package fr.waveme.backend.crud.dto;

import fr.waveme.backend.crud.models.enumerators.EReportReason;
import fr.waveme.backend.crud.models.enumerators.EReportStatus;

import java.time.LocalDateTime;

public class ReportDto {
    public Long id;
    public Long reporterId;
    public Long commentId;
    public Long replyId;
    public EReportReason reason;
    public String description;
    public EReportStatus status;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}
