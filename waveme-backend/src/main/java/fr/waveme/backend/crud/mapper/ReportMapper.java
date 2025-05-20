package fr.waveme.backend.crud.mapper;

import fr.waveme.backend.crud.dto.ReportDto;
import fr.waveme.backend.crud.models.Report;

public class ReportMapper {

    public static ReportDto toDto(Report report) {
        ReportDto dto = new ReportDto();
        dto.id = report.getId();
        dto.reporterId = report.getReporter().getId();
        dto.commentId = report.getComment() != null ? report.getComment().getId() : null;
        dto.replyId = report.getReply() != null ? report.getReply().getId() : null;
        dto.reason = report.getReason();
        dto.description = report.getDescription();
        dto.status = report.getStatus();
        dto.createdAt = report.getCreatedAt();
        dto.updatedAt = report.getUpdatedAt();
        return dto;
    }
}
