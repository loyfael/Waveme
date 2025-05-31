package fr.waveme.backend.social.crud.service;

import fr.waveme.backend.social.crud.dto.ReportDto;

import java.util.List;

public interface ReportService {
  ReportDto createReport(ReportDto reportDto);
  ReportDto getReportById(String id); // changé de Long à String
  List<ReportDto> getAllReports();
  void deleteReport(String id); // changé de Long à String
  ReportDto updateStatus(String id, String newStatus); // changé de Long à String
}