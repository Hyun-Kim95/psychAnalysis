package com.tst.psychAnalysis.admin;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class AdminResponseDetail {

    private final UUID sessionId;
    private final LocalDateTime submittedAt;
    private final String groupCode;
    private final Double totalRawScore;
    private final Double totalTScore;
    private final Map<String, Double> scaleRawScores;
    private final Map<String, Double> scaleTScores;

    public AdminResponseDetail(UUID sessionId,
                               LocalDateTime submittedAt,
                               String groupCode,
                               Double totalRawScore,
                               Double totalTScore,
                               Map<String, Double> scaleRawScores,
                               Map<String, Double> scaleTScores) {
        this.sessionId = sessionId;
        this.submittedAt = submittedAt;
        this.groupCode = groupCode;
        this.totalRawScore = totalRawScore;
        this.totalTScore = totalTScore;
        this.scaleRawScores = scaleRawScores;
        this.scaleTScores = scaleTScores;
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

    public Map<String, Double> getScaleRawScores() {
        return scaleRawScores;
    }

    public Map<String, Double> getScaleTScores() {
        return scaleTScores;
    }
}

