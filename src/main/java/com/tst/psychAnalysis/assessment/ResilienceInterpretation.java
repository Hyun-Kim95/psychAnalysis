package com.tst.psychAnalysis.assessment;

import java.util.Map;

/**
 * 회복탄력성 검사 척도 "R"의 T점수 구간에 따른 해석.
 * T점수 &lt; 43 낮음, 43~57 평균, &gt; 57 높음.
 */
public final class ResilienceInterpretation {

    private static final double BAND_LOW_MAX = 43;
    private static final double BAND_HIGH_MIN = 57;

    private static final String LOW = "회복탄력성이 상대적으로 낮은 편으로 해석됩니다. 스트레스나 역경에 민감하게 반응하거나, 회복에 시간이 걸릴 수 있습니다. 지지 체계(가족, 친구, 전문가)를 활용하고, 작은 성취를 인정하는 습관이 도움이 될 수 있습니다.";
    private static final String AVERAGE = "회복탄력성이 평균 범위입니다. 어려움이 있어도 적당히 대처하고 회복해 나가는 편입니다. 지지 체계와 자기 돌봄을 이어가시면 좋습니다.";
    private static final String HIGH = "회복탄력성이 높은 편으로 해석됩니다. 스트레스나 역경을 겪어도 비교적 잘 적응하고, 경험에서 의미를 찾으며 회복하는 경향이 있습니다. 이러한 강점을 일상에서 활용해 보시기 바랍니다.";

    /**
     * 총점(TOTAL) 또는 단일척도 "R"의 T점수에 따른 해석을 반환합니다. KRQ-53은 TOTAL 사용.
     */
    public static Map<String, String> interpret(Map<String, Double> scaleTScores) {
        Double t = scaleTScores != null ? (scaleTScores.get("TOTAL") != null ? scaleTScores.get("TOTAL") : scaleTScores.get("R")) : null;
        String text = interpretOne(t);
        if (text.isEmpty()) return Map.of();
        return scaleTScores != null && scaleTScores.containsKey("TOTAL") ? Map.of("TOTAL", text) : Map.of("R", text);
    }

    public static String interpretOne(Double tScore) {
        if (tScore == null) return "";
        if (tScore < BAND_LOW_MAX) return LOW;
        if (tScore > BAND_HIGH_MIN) return HIGH;
        return AVERAGE;
    }

    /** 관리자용: T점수 구간 및 해석 문구 */
    public static String getReferenceDescription() {
        return "T점수 구간: <43 낮음, 43~57 평균, >57 높음\n"
                + "· 낮음: " + LOW + "\n"
                + "· 평균: " + AVERAGE + "\n"
                + "· 높음: " + HIGH;
    }

    private ResilienceInterpretation() {}
}
