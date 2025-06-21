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
            .reporterId(report.getReporterId()) // Long
            .reportedUserId(report.getReportedUserId())
            .postId(report.getPost() != null ? report.getPost().getId() : null)
            .commentId(report.getComment() != null ? report.getComment().getId() : null)
            .replyId(report.getReply() != null ? report.getReply().getId() : null)
            .reason(report.getReason())
            .description(report.getDescription())
            .status(report.getStatus())
            .createdAt(report.getCreatedAt())
            .updatedAt(report.getUpdatedAt())
            .build();
  }

  public Report toEntity(ReportDto dto, Comment comment, Reply reply, Post post) {
    if (dto == null) return null;

    String reportedUserId = null;
    if (comment != null) {
      reportedUserId = comment.getUserId();
    } else if (reply != null) {
      reportedUserId = reply.getUserId();
    }

    return Report.builder()
            .id(dto.getId())
            .reporterId(dto.getReporterId())
            .comment(comment)
            .post(post)
            .reply(reply)
            .reportedUserId(dto.getReportedUserId() != null ? dto.getReportedUserId() : reportedUserId)
            .reason(dto.getReason())
            .description(dto.getDescription())
            .status(dto.getStatus() != null ? dto.getStatus() : EReportStatus.PENDING)
            .createdAt(dto.getCreatedAt())
            .updatedAt(dto.getUpdatedAt())
            .build();
  }

}
