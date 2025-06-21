package fr.waveme.backend.social.crud.mapper;

import fr.waveme.backend.social.crud.dto.ReportDto;
import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.models.Reply;
import fr.waveme.backend.social.crud.models.Report;
import fr.waveme.backend.social.crud.models.enumerator.EReportStatus;
import org.springframework.stereotype.Component;

/**
 * ReportMapper is a component that maps between Report entities and ReportDto objects.
 * It provides methods to convert a Report to a ReportDto and vice versa.
 */
@Component
public class ReportMapper {

  public ReportDto toDto(Report report) {
    if (report == null) return null;

    return ReportDto.builder()
            .id(report.getId())
            .reporterId(report.getReporterId())
            .reportedUserId(report.getReportedUserId())
            .postId(report.getPostId())
            .commentId(report.getCommentId())
            .replyId(report.getReplyId())
            .reason(report.getReason())
            .description(report.getDescription())
            .status(report.getStatus())
            .createdAt(report.getCreatedAt())
            .updatedAt(report.getUpdatedAt())
            .build();
  }

  public Report toEntity(ReportDto dto) {
    if (dto == null) return null;

    return Report.builder()
            .id(dto.getId())
            .reporterId(dto.getReporterId())
            .reportedUserId(dto.getReportedUserId())
            .postId(dto.getPostId())
            .commentId(dto.getCommentId())
            .replyId(dto.getReplyId())
            .reason(dto.getReason())
            .description(dto.getDescription())
            .status(dto.getStatus() != null ? dto.getStatus() : EReportStatus.PENDING)
            .createdAt(dto.getCreatedAt())
            .updatedAt(dto.getUpdatedAt())
            .build();
  }

}
