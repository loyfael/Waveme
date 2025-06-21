package fr.waveme.backend.social.crud.controller;

import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.dto.ReportDto;
import fr.waveme.backend.social.crud.dto.pub.report.ReportCreateDto;
import fr.waveme.backend.social.crud.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
  private final ReportService reportService;
  private final JwtUtils jwtUtils;

  @PostMapping("/comment/{commentId}")
  public ResponseEntity<ReportDto> reportComment(
          @PathVariable String commentId,
          @RequestBody ReportCreateDto request,
          @RequestHeader("Authorization") String authorizationHeader
  ) {
    String token = authorizationHeader.replace("Bearer ", "");
    String reporterId = jwtUtils.getSocialUserIdFromJwtToken(token);

    ReportDto dto = ReportDto.builder()
            .reporterId(Long.valueOf(reporterId))
            .commentId(Long.valueOf(commentId))
            .reason(request.getReason())
            .description(request.getDescription())
            .build();

    return ResponseEntity.ok(reportService.createReport(dto));
  }

  @PostMapping("/reply/{replyId}")
  public ResponseEntity<ReportDto> reportReply(
          @PathVariable String replyId,
          @RequestBody ReportCreateDto request,
          @RequestHeader("Authorization") String authorizationHeader
  ) {
    String token = authorizationHeader.replace("Bearer ", "");
    String reporterId = jwtUtils.getSocialUserIdFromJwtToken(token);

    ReportDto dto = ReportDto.builder()
            .reporterId(Long.valueOf(reporterId))
            .replyId(Long.valueOf(replyId))
            .reason(request.getReason())
            .description(request.getDescription())
            .build();

    return ResponseEntity.ok(reportService.createReport(dto));
  }

  @PostMapping("/user/{userId}")
  public ResponseEntity<ReportDto> reportUser(
          @PathVariable String userId,
          @RequestBody ReportCreateDto request,
          @RequestHeader("Authorization") String authorizationHeader
  ) {
    String token = authorizationHeader.replace("Bearer ", "");
    String reporterId = jwtUtils.getSocialUserIdFromJwtToken(token);

    ReportDto dto = ReportDto.builder()
            .reporterId(Long.valueOf(reporterId))
            .reportedUserId(Long.valueOf(userId))
            .reason(request.getReason())
            .description(request.getDescription())
            .build();

    return ResponseEntity.ok(reportService.createReport(dto));
  }

  @PostMapping("/post/{postId}")
  public ResponseEntity<ReportDto> reportPost(
          @PathVariable String postId,
          @RequestBody ReportCreateDto request,
          @RequestHeader("Authorization") String authorizationHeader
  ) {
    String token = authorizationHeader.replace("Bearer ", "");
    String reporterId = jwtUtils.getSocialUserIdFromJwtToken(token);

    ReportDto dto = ReportDto.builder()
            .reporterId(Long.valueOf(reporterId))
            .postId(Long.valueOf(postId))
            .reason(request.getReason())
            .description(request.getDescription())
            .build();

    return ResponseEntity.ok(reportService.createReport(dto));
  }

  @GetMapping
  public ResponseEntity<List<ReportDto>> getAllReports() {
    return ResponseEntity.ok(reportService.getAllReports());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ReportDto> getReportById(@PathVariable String id) {
    ReportDto report = reportService.getReportById(id);
    return report != null ? ResponseEntity.ok(report) : ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteReport(@PathVariable String id) {
    reportService.deleteReport(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<ReportDto> updateStatus(
          @PathVariable String id,
          @RequestParam String status
  ) {
    ReportDto updated = reportService.updateStatus(id, status);
    return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
  }

  @GetMapping("/target/{userId}")
  public ResponseEntity<List<ReportDto>> getReportsByTargetUser(@PathVariable String userId) {
    return ResponseEntity.ok(reportService.getReportsByReportedUserId(userId));
  }
}
