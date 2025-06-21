package fr.waveme.backend.social.crud.service.impl;

import fr.waveme.backend.social.crud.dto.ReportDto;
import fr.waveme.backend.social.crud.exception.UserAutoReportException;
import fr.waveme.backend.social.crud.mapper.ReportMapper;
import fr.waveme.backend.social.crud.models.Comment;
import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.models.Reply;
import fr.waveme.backend.social.crud.models.Report;
import fr.waveme.backend.social.crud.models.enumerator.EReportStatus;
import fr.waveme.backend.social.crud.repository.CommentRepository;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.social.crud.repository.ReplyRepository;
import fr.waveme.backend.social.crud.repository.ReportRepository;
import fr.waveme.backend.social.crud.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ReportServiceImpl provides the implementation of the ReportService interface.
 * It handles CRUD operations for reports, including creating, retrieving, updating, and deleting reports.
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

  private final ReportRepository reportRepository;
  private final CommentRepository commentRepository;
  private final ReplyRepository replyRepository;
  private final ReportMapper reportMapper;
  private final PostRepository postRepository;

  @Override
  public ReportDto createReport(ReportDto dto) {

    // Sécurité métier : un seul type de contenu signalé à la fois
    int nonNullTargets = 0;
    if (dto.getPostId() != null) nonNullTargets++;
    if (dto.getCommentId() != null) nonNullTargets++;
    if (dto.getReplyId() != null) nonNullTargets++;
    if (dto.getReportedUserId() != null) nonNullTargets++;

    if (nonNullTargets != 1) {
      throw new IllegalArgumentException("Un report doit cibler uniquement un post, un commentaire, une réponse OU un utilisateur.");
    }

    // Extraction automatique du reportedUserId si nécessaire
    if (dto.getCommentId() != null) {
      Comment comment = commentRepository.findByCommentUniqueId(dto.getCommentId()).orElseThrow();
      dto.setCommentId(comment.getCommentUniqueId());
      dto.setReportedUserId(Long.valueOf(comment.getUserId()));
    }

    if (dto.getReplyId() != null) {
      Reply reply = replyRepository.findByReplyUniqueId(dto.getReplyId()).orElseThrow();
      dto.setReplyId(reply.getReplyUniqueId());
      dto.setReportedUserId(Long.valueOf(reply.getUserId()));
    }

    if (dto.getPostId() != null) {
      Post post = postRepository.findByPostUniqueId(dto.getPostId()).orElseThrow();
      dto.setPostId(post.getPostUniqueId());
      dto.setReportedUserId(Long.valueOf(post.getUserId()));
    }

    // Empêcher l'auto-signalement
//    if (dto.getReporterId() != null && dto.getReporterId().equals(dto.getReportedUserId())) {
//      throw new UserAutoReportException("Vous ne pouvez pas signaler votre propre contenu.");
//    }

    Report report = reportMapper.toEntity(dto);
    return reportMapper.toDto(reportRepository.save(report));
  }

  @Override
  public ReportDto getReportById(String id) {
    return reportRepository.findById(id)
            .map(reportMapper::toDto)
            .orElse(null);
  }

  @Override
  public List<ReportDto> getAllReports() {
    return reportRepository.findAll()
            .stream()
            .map(reportMapper::toDto)
            .collect(Collectors.toList());
  }

  @Override
  public void deleteReport(String id) {
    reportRepository.deleteById(id);
  }

  @Override
  public ReportDto updateStatus(String id, String newStatus) {
    Optional<Report> optionalReport = reportRepository.findById(id);
    if (optionalReport.isEmpty()) return null;

    Report report = optionalReport.get();
    report.setStatus(EReportStatus.valueOf(newStatus));
    return reportMapper.toDto(reportRepository.save(report));
  }

  @Override
  public List<ReportDto> getReportsByReportedUserId(String reportedUserId) {
    return reportRepository.findByReportedUserId(reportedUserId)
            .stream()
            .map(reportMapper::toDto)
            .collect(Collectors.toList());
  }

  @Override
  public Report getByUniqueReportId(Long reportUniqueId) {
        return reportRepository.findByReportUniqueId(reportUniqueId);
  }
}
