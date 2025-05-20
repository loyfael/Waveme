package fr.waveme.backend.crud.service.impl;

import fr.waveme.backend.crud.dto.ReportDto;
import fr.waveme.backend.crud.mapper.ReportMapper;
import fr.waveme.backend.crud.models.Comment;
import fr.waveme.backend.crud.models.Reply;
import fr.waveme.backend.crud.models.Report;
import fr.waveme.backend.crud.models.User;
import fr.waveme.backend.crud.repository.CommentRepository;
import fr.waveme.backend.crud.repository.ReplyRepository;
import fr.waveme.backend.crud.repository.ReportRepository;
import fr.waveme.backend.crud.repository.UserRepository;
import fr.waveme.backend.crud.service.ReportService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    public ReportServiceImpl(ReportRepository reportRepository, UserRepository userRepository,
                             CommentRepository commentRepository, ReplyRepository replyRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.replyRepository = replyRepository;
    }

    @Override
    public ReportDto getById(Long id) {
        return reportRepository.findById(id)
                .map(ReportMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Report not found"));
    }

    @Override
    public List<ReportDto> getAll() {
        return reportRepository.findAll().stream()
                .map(ReportMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReportDto create(ReportDto dto) {
        Report report = new Report();

        User reporter = userRepository.findById(dto.reporterId)
                .orElseThrow(() -> new EntityNotFoundException("Reporter not found"));
        report.setReporter(reporter);

        if (dto.commentId != null) {
            Comment comment = commentRepository.findById(dto.commentId)
                    .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
            report.setComment(comment);
        }

        if (dto.replyId != null) {
            Reply reply = replyRepository.findById(dto.replyId)
                    .orElseThrow(() -> new EntityNotFoundException("Reply not found"));
            report.setReply(reply);
        }

        if (dto.commentId != null && dto.replyId != null) {
            throw new IllegalArgumentException("A report cannot target both a comment and a reply.");
        }

        report.setReason(dto.reason);
        report.setDescription(dto.description);
        report.setStatus(dto.status);

        return ReportMapper.toDto(reportRepository.save(report));
    }
}
