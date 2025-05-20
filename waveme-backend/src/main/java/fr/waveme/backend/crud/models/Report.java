package fr.waveme.backend.crud.models;

import fr.waveme.backend.crud.models.enumerators.EReportReason;
import fr.waveme.backend.crud.models.enumerators.EReportStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who reported the content
    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    // The reported content is either a comment or a reply ?
    // It's a comment !
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    // It's a reply !
    @ManyToOne
    @JoinColumn(name = "reply_id")
    private Reply reply;

    // Raison du signalement (enum typée)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EReportReason reason;

    // Description facultative
    @Column(columnDefinition = "TEXT")
    private String description;

    // Statut du traitement du report
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EReportStatus status = EReportStatus.PENDING;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
