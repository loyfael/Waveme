package fr.waveme.backend.social.crud.service;

import fr.waveme.backend.social.crud.dto.ReportDto;
import fr.waveme.backend.social.crud.models.Report;

import java.util.List;

/**
 * ReportService provides methods to perform CRUD operations on Report entities.
 * It contains methods to create, retrieve, update, and delete reports.
 */
public interface ReportService {
  ReportDto createReport(ReportDto reportDto);
  ReportDto getReportById(String id);
  Report getByUniqueReportId(Long reportUniqueId);
  List<ReportDto> getAllReports();
  void deleteReport(String id);
  ReportDto updateStatus(String id, String newStatus);
  List<ReportDto> getReportsByReportedUserId(String reportedUserId);
}