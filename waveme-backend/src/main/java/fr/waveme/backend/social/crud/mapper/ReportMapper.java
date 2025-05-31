package fr.waveme.backend.social.crud.mapper;

import fr.waveme.backend.social.crud.dto.ReportDto;
import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.models.Reply;
import fr.waveme.backend.social.crud.models.Report;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

  public ReportDto toDto(Report report) {
    if (report == null) return null;

    return ReportDto.builder()
            .id(report.getId())
            .reporterId(report.getReporterId()) // Long
            .commentId(report.getComment() != null ? report.getComment().getId() : null)
            .replyId(report.getReply() != null ? report.getReply().getId() : null)
            .reason(report.getReason())
            .description(report.getDescription())
            .status(report.getStatus())
            .createdAt(report.getCreatedAt())
            .updatedAt(report.getUpdatedAt())
            .build();
  }

  public Report toEntity(ReportDto dto, Comment comment, Reply reply) {
    if (dto == null) return null;

    return Report.builder()
            .id(dto.getId()) // String
            .reporterId(dto.getReporterId()) // Long
            .comment(comment)
            .reply(reply)
            .reason(dto.getReason())
            .description(dto.getDescription())
            .status(dto.getStatus())
            .createdAt(dto.getCreatedAt())
            .updatedAt(dto.getUpdatedAt())
            .build();
  }
}
