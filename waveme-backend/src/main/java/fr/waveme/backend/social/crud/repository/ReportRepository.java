package fr.waveme.backend.social.crud.repository;

import fr.waveme.backend.social.crud.models.Report;
import fr.waveme.backend.social.crud.models.enumerator.EReportStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * ReportRepository provides methods to perform CRUD operations on Report entities.
 * It extends MongoRepository to leverage built-in methods for MongoDB interactions.
 */
public interface ReportRepository extends MongoRepository<Report, String> {

  List<Report> findByReporterId(String reporterId);
  List<Report> findByStatus(EReportStatus status);
  Report findByReportUniqueId(Long reportUniqueId);
  List<Report> findByPostId(String postId);
  List<Report> findByCommentId(String commentId);
  List<Report> findByReplyId(String replyId);
  List<Report> findByReportedUserId(String reportedUserId);
}
