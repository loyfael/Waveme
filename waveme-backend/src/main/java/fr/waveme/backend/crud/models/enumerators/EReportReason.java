package fr.waveme.backend.crud.models.enumerators;

import lombok.Getter;

/**
 * Enum representing the reasons for reporting a user.
 * Each reason is associated with a string description.
 */
@Getter
public enum EReportReason {
    SPAM("Spam"),
    HARASSMENT("Harassment"),
    INAPPROPRIATE_CONTENT("Inappropriate Content"),
    IMPERSONATION("Impersonation"),
    OTHER("Other");

    private final String reason;

    EReportReason(String reason) {
        this.reason = reason;
    }

}
