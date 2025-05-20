package fr.waveme.backend.crud.service;

import fr.waveme.backend.crud.dto.ReportDto;

import java.util.List;

public interface ReportService {
    ReportDto getById(Long id);
    List<ReportDto> getAll();
    ReportDto create(ReportDto dto);
}
