package com.tst.psychAnalysis.response;

import java.util.List;
import java.util.Map;

public class ResultResponse {

    /** 실시한 검사명 */
    private final String assessmentName;
    private final Double totalRawScore;
    private final Double totalTScore;
    private final Map<String, Double> scaleRawScores;
    private final Map<String, Double> scaleTScores;
    /** 척도 표시 순서 (검사별 DB scale id 순) */
    private final List<String> scaleOrder;
    /** 척도 한글명 (code -> 한글). 검사별 scale.name 사용 */
    private final Map<String, String> scaleDisplayNames;
    /** 척도별 점수 해석 (code -> 해석 문장). TCI만 제공 */
    private final Map<String, String> scaleInterpretations;
    /** NEO 등: 주척도별 그룹(예: N (신경증) → [N1,N2,...]). 없으면 null 또는 빈 리스트 */
    private final List<ScaleGroupDto> scaleGroups;

    public ResultResponse(String assessmentName,
                          Double totalRawScore,
                          Double totalTScore,
                          Map<String, Double> scaleRawScores,
                          Map<String, Double> scaleTScores,
                          List<String> scaleOrder,
                          Map<String, String> scaleDisplayNames,
                          Map<String, String> scaleInterpretations,
                          List<ScaleGroupDto> scaleGroups) {
        this.assessmentName = assessmentName != null ? assessmentName : "";
        this.totalRawScore = totalRawScore;
        this.totalTScore = totalTScore;
        this.scaleRawScores = scaleRawScores;
        this.scaleTScores = scaleTScores;
        this.scaleOrder = scaleOrder != null ? scaleOrder : List.of();
        this.scaleDisplayNames = scaleDisplayNames != null ? scaleDisplayNames : Map.of();
        this.scaleInterpretations = scaleInterpretations != null ? scaleInterpretations : Map.of();
        this.scaleGroups = scaleGroups != null ? scaleGroups : List.of();
    }

    /** scaleGroups 없이 호출 시 빈 리스트로 설정 (기존 호환) */
    public ResultResponse(String assessmentName,
                          Double totalRawScore,
                          Double totalTScore,
                          Map<String, Double> scaleRawScores,
                          Map<String, Double> scaleTScores,
                          List<String> scaleOrder,
                          Map<String, String> scaleDisplayNames,
                          Map<String, String> scaleInterpretations) {
        this(assessmentName, totalRawScore, totalTScore, scaleRawScores, scaleTScores,
             scaleOrder, scaleDisplayNames, scaleInterpretations, List.of());
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

    public Map<String, String> getScaleInterpretations() {
        return scaleInterpretations;
    }

    public List<ScaleGroupDto> getScaleGroups() {
        return scaleGroups;
    }
}

