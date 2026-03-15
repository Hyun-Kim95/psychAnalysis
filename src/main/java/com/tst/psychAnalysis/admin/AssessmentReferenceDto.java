package com.tst.psychAnalysis.admin;

import com.tst.psychAnalysis.response.ScaleGroupDto;

import java.util.List;

/**
 * 검사별 기준점수(규준) 및 해석 기준 정보.
 * scaleGroups: NEO 등 주척도별 그룹(검사결과 화면과 동일한 N·E·O·A·C + N1~C6 순서 표시용).
 */
public record AssessmentReferenceDto(
        String assessmentName,
        List<NormRowDto> norms,
        String interpretationGuide,
        List<ScaleGroupDto> scaleGroups
) {
    public record NormRowDto(String scaleCode, String scaleName, Double mean, Double sd) {}

    /** scaleGroups 없이 생성 시 빈 리스트 사용 (기존 호환) */
    public AssessmentReferenceDto(String assessmentName, List<NormRowDto> norms, String interpretationGuide) {
        this(assessmentName, norms, interpretationGuide, List.of());
    }
}
