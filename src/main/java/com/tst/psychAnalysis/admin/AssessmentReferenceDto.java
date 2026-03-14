package com.tst.psychAnalysis.admin;

import java.util.List;

/**
 * 검사별 기준점수(규준) 및 해석 기준 정보.
 */
public record AssessmentReferenceDto(
        String assessmentName,
        List<NormRowDto> norms,
        String interpretationGuide
) {
    public record NormRowDto(String scaleCode, String scaleName, Double mean, Double sd) {}
}
