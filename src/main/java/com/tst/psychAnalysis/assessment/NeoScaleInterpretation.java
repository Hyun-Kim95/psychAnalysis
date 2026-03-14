package com.tst.psychAnalysis.assessment;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * NEO 성격검사 척도별 T점수 구간에 따른 해석.
 * T점수 &lt; 43 낮음, 43~57 평균, &gt; 57 높음.
 */
public final class NeoScaleInterpretation {

    private static final double BAND_LOW_MAX = 43;
    private static final double BAND_HIGH_MIN = 57;

    /** 척도 표시 순서: N, E, O, A, C */
    public static final java.util.List<String> SCALE_ORDER = java.util.List.of("N", "E", "O", "A", "C");

    private static final Map<String, Map<String, String>> TEXTS = buildTexts();

    private static Map<String, Map<String, String>> buildTexts() {
        Map<String, Map<String, String>> out = new LinkedHashMap<>();
        put(out, "N", "정서가 비교적 안정적인 편입니다. 스트레스나 좌절에 덜 흔들리며, 걱정이나 불안이 적은 경향이 있습니다.",
            "신경증 수준이 평균 범위입니다. 때로는 걱정이나 기분 변화가 있으면서도 전반적으로 적응해 나가는 편입니다.",
            "정서가 예민하고 스트레스에 민감한 편입니다. 걱정, 불안, 기분 저하 등을 자주 느낄 수 있으며, 감정 기복이 있을 수 있습니다.");
        put(out, "E", "내향적 성향이 있는 편입니다. 혼자 있는 시간을 편하게 느끼며, 사람이 많은 자리보다 소수와의 교류를 선호할 수 있습니다.",
            "외향성 수준이 평균 범위입니다. 상황에 따라 사교적이기도 하고 조용히 있기도 하는 균형이 있습니다.",
            "외향적이고 사교적인 편입니다. 사람들과 함께 있을 때 에너지를 얻으며, 적극적으로 소통하고 활동하는 경향이 있습니다.");
        put(out, "O", "현실적이고 익숙한 방식을 선호하는 편입니다. 새로운 아이디어나 추상적 주제보다는 구체적·실용적인 것에 익숙할 수 있습니다.",
            "개방성 수준이 평균 범위입니다. 새로운 경험과 아이디어에 적당히 열려 있으면서도 익숙한 것도 편하게 느낍니다.",
            "새로운 경험과 아이디어에 열려 있는 편입니다. 상상력과 호기심이 많고, 예술·자연·다양한 관점에 관심이 있습니다.");
        put(out, "A", "자기 주장이 강하거나 거리감이 있는 편일 수 있습니다. 타인과의 조화보다 원칙이나 효율을 우선할 수 있습니다.",
            "친화성 수준이 평균 범위입니다. 타인을 배려하면서도 자신의 입장을 지키는 균형이 있습니다.",
            "따뜻하고 협력적인 편입니다. 타인의 입장을 이해하고 배려하며, 조화와 협력을 중요하게 여깁니다.");
        put(out, "C", "유연하고 즉흥적인 편일 수 있습니다. 계획보다는 상황에 맞춰 움직이거나, 규칙을 엄격히 지키기보다 여유 있게 대할 수 있습니다.",
            "성실성 수준이 평균 범위입니다. 목표를 세우고 노력하면서도, 필요 시 유연하게 대처하는 편입니다.",
            "계획적이고 책임감이 있는 편입니다. 목표를 세우고 꾸준히 노력하며, 약속과 규칙을 지키려 합니다.");
        return out;
    }

    private static void put(Map<String, Map<String, String>> target, String scale, String low, String average, String high) {
        Map<String, String> band = new LinkedHashMap<>();
        band.put("low", low);
        band.put("average", average);
        band.put("high", high);
        target.put(scale, band);
    }

    public static String interpret(String scaleCode, Double tScore) {
        if (tScore == null) return "";
        Map<String, String> band = TEXTS.get(scaleCode);
        if (band == null) return "";
        if (tScore < BAND_LOW_MAX) return band.get("low");
        if (tScore > BAND_HIGH_MIN) return band.get("high");
        return band.get("average");
    }

    public static Map<String, String> interpretAll(Map<String, Double> scaleTScores) {
        Map<String, String> out = new LinkedHashMap<>();
        for (String code : SCALE_ORDER) {
            String text = interpret(code, scaleTScores != null ? scaleTScores.get(code) : null);
            if (!text.isEmpty()) out.put(code, text);
        }
        return out;
    }

    private static final Map<String, String> SCALE_NAMES = Map.of(
            "N", "신경증", "E", "외향성", "O", "개방성", "A", "친화성", "C", "성실성");

    /** 관리자용: T점수 구간 및 척도별 해석 문구 */
    public static String getReferenceDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("T점수 구간: <43 낮음, 43~57 평균, >57 높음 (척도별 적용)\n");
        for (String code : SCALE_ORDER) {
            Map<String, String> band = TEXTS.get(code);
            if (band == null) continue;
            String name = SCALE_NAMES.getOrDefault(code, code);
            sb.append("\n· ").append(code).append(" (").append(name).append(")\n");
            sb.append("  낮음: ").append(band.get("low")).append("\n");
            sb.append("  평균: ").append(band.get("average")).append("\n");
            sb.append("  높음: ").append(band.get("high")).append("\n");
        }
        return sb.toString();
    }

    private NeoScaleInterpretation() {}
}
