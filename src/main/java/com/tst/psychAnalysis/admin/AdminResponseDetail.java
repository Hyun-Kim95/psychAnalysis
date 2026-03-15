package com.tst.psychAnalysis.admin;

import com.tst.psychAnalysis.response.ScaleGroupDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AdminResponseDetail {

    private final UUID sessionId;
    private final UUID resultId;
    private final LocalDateTime submittedAt;
    private final String groupCode;
    private final String assessmentName;
    private final Double totalRawScore;
    private final Double totalTScore;
    private final Map<String, Double> scaleRawScores;
    private final Map<String, Double> scaleTScores;
    /** 척도 표시 순서 (결과 화면과 동일). 없으면 null/빈 리스트 */
    private final List<String> scaleOrder;
    /** 척도 코드 → 한글명. 없으면 null/빈 맵 */
    private final Map<String, String> scaleDisplayNames;
    /** NEO 등 주척도별 그룹. 없으면 null/빈 리스트 */
    private final List<ScaleGroupDto> scaleGroups;

    public AdminResponseDetail(UUID sessionId,
                               UUID resultId,
                               LocalDateTime submittedAt,
                               String groupCode,
                               String assessmentName,
                               Double totalRawScore,
                               Double totalTScore,
                               Map<String, Double> scaleRawScores,
                               Map<String, Double> scaleTScores,
                               List<String> scaleOrder,
                               Map<String, String> scaleDisplayNames,
                               List<ScaleGroupDto> scaleGroups) {
        this.sessionId = sessionId;
        this.resultId = resultId;
        this.submittedAt = submittedAt;
        this.groupCode = groupCode;
        this.assessmentName = assessmentName;
        this.totalRawScore = totalRawScore;
        this.totalTScore = totalTScore;
        this.scaleRawScores = scaleRawScores;
        this.scaleTScores = scaleTScores;
        this.scaleOrder = scaleOrder != null ? scaleOrder : List.of();
        this.scaleDisplayNames = scaleDisplayNames != null ? scaleDisplayNames : Map.of();
        this.scaleGroups = scaleGroups != null ? scaleGroups : List.of();
    }

    /** 기존 호환: scaleOrder/scaleDisplayNames/scaleGroups 없이 생성 시 빈 값 */
    public AdminResponseDetail(UUID sessionId,
                               UUID resultId,
                               LocalDateTime submittedAt,
                               String groupCode,
                               String assessmentName,
                               Double totalRawScore,
                               Double totalTScore,
                               Map<String, Double> scaleRawScores,
                               Map<String, Double> scaleTScores) {
        this(sessionId, resultId, submittedAt, groupCode, assessmentName, totalRawScore, totalTScore,
             scaleRawScores, scaleTScores, List.of(), Map.of(), List.of());
    }

    public UUID getResultId() {
        return resultId;
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

    public String getAssessmentName() {
        return assessmentName;
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

    public List<String> getScaleOrder() {
        return scaleOrder;
    }

    public Map<String, String> getScaleDisplayNames() {
        return scaleDisplayNames;
    }

    public List<ScaleGroupDto> getScaleGroups() {
        return scaleGroups;
    }
}

