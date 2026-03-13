package com.tst.psychAnalysis.response;

import java.util.Map;

public class ResultResponse {

    private final Double totalRawScore;
    private final Double totalTScore;
    private final Map<String, Double> scaleRawScores;
    private final Map<String, Double> scaleTScores;

    public ResultResponse(Double totalRawScore,
                          Double totalTScore,
                          Map<String, Double> scaleRawScores,
                          Map<String, Double> scaleTScores) {
        this.totalRawScore = totalRawScore;
        this.totalTScore = totalTScore;
        this.scaleRawScores = scaleRawScores;
        this.scaleTScores = scaleTScores;
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

