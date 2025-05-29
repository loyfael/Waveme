package fr.waveme.backend.social.crud.repository;

import fr.waveme.backend.social.crud.models.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReportRepository extends MongoRepository<Report, String> {

  List<Report> findByReporterId(String reporterId);
  List<Report> findByStatus(String status);
}
