package com.tst.psychAnalysis.admin;

import java.time.LocalDateTime;
import java.util.UUID;

public class AdminResponseSummary {

    private final UUID sessionId;
    private final LocalDateTime submittedAt;
    private final String groupCode;
    private final Double totalRawScore;
    private final Double totalTScore;

    public AdminResponseSummary(UUID sessionId,
                                LocalDateTime submittedAt,
                                String groupCode,
                                Double totalRawScore,
                                Double totalTScore) {
        this.sessionId = sessionId;
        this.submittedAt = submittedAt;
        this.groupCode = groupCode;
        this.totalRawScore = totalRawScore;
        this.totalTScore = totalTScore;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public Double getTotalRawScore() {
        return totalRawScore;
    }

    public Double getTotalTScore() {
        return totalTScore;
    }
}

