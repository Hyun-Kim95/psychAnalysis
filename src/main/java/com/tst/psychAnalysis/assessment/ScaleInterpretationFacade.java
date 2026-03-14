package com.tst.psychAnalysis.assessment;

import java.util.Map;

/**
 * 검사명에 따라 적절한 척도별 해석을 반환하는 진입점.
 */
public final class ScaleInterpretationFacade {

    /**
     * 검사명, 총점, 척도 원점수/ T점수 맵을 주면 해당 검사의 척도별 해석 맵을 반환합니다.
     * TCI·NEO·회복탄력성은 T점수 구간, BDI·BAI는 총 원점수 구간을 사용합니다.
     */
    public static Map<String, String> interpret(
            String assessmentName,
            Double totalRawScore,
            Map<String, Double> scaleRawScores,
            Map<String, Double> scaleTScores) {
        if (assessmentName == null) return Map.of();

        if (assessmentName.contains("TCI")) {
            return TciScaleInterpretation.interpretAll(scaleTScores);
        }
        if (assessmentName.contains("BDI")) {
            return BdiInterpretation.interpret(totalRawScore);
        }
        if (assessmentName.contains("BAI")) {
            return BaiInterpretation.interpret(totalRawScore);
        }
        if (assessmentName.contains("NEO")) {
            return NeoScaleInterpretation.interpretAll(scaleTScores);
        }
        if (assessmentName.contains("회복탄력성")) {
            return ResilienceInterpretation.interpret(scaleTScores);
        }

        return Map.of();
    }

    private ScaleInterpretationFacade() {}
}
