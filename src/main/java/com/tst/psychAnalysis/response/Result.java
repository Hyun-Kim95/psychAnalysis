package com.tst.psychAnalysis.response;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Result {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "response_session_id")
    private ResponseSession responseSession;

    @Column(name = "total_raw_score")
    private Double totalRawScore;

    @Column(name = "total_t_score")
    private Double totalTScore;

    @Column(name = "scale_raw_scores")
    private String scaleRawScoresJson;

    @Column(name = "scale_t_scores")
    private String scaleTScoresJson;

    private String summaryText;

    private LocalDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ResponseSession getResponseSession() {
        return responseSession;
    }

    public void setResponseSession(ResponseSession responseSession) {
        this.responseSession = responseSession;
    }

    public Double getTotalRawScore() {
        return totalRawScore;
    }

    public void setTotalRawScore(Double totalRawScore) {
        this.totalRawScore = totalRawScore;
    }

    public Double getTotalTScore() {
        return totalTScore;
    }

    public void setTotalTScore(Double totalTScore) {
        this.totalTScore = totalTScore;
    }

    public String getScaleRawScoresJson() {
        return scaleRawScoresJson;
    }

    public void setScaleRawScoresJson(String scaleRawScoresJson) {
        this.scaleRawScoresJson = scaleRawScoresJson;
    }

    public String getScaleTScoresJson() {
        return scaleTScoresJson;
    }

    public void setScaleTScoresJson(String scaleTScoresJson) {
        this.scaleTScoresJson = scaleTScoresJson;
    }

    public String getSummaryText() {
        return summaryText;
    }

    public void setSummaryText(String summaryText) {
        this.summaryText = summaryText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

