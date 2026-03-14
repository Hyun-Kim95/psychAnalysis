package com.tst.psychAnalysis.assessment;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * TCI 척도별 T점수 구간에 따른 해석 문구.
 */
public final class TciScaleInterpretation {

    private static final double BAND_LOW_MAX = 43;
    private static final double BAND_HIGH_MIN = 57;

    /** 척도별 구간 해석: "low" / "average" / "high" -> 문장 */
    private static final Map<String, Map<String, String>> TEXTS = new LinkedHashMap<>();

    static {
        put("NS", "새로운 것에 대한 욕구가 낮은 편입니다. 익숙한 환경과 일상을 선호하며, 변화보다는 안정을 추구할 수 있습니다.",
            "새로움 추구 수준이 평균 범위입니다. 새로운 경험에 적당히 열려 있으면서도 일상의 안정도 유지하는 편입니다.",
            "새로운 자극과 경험에 대한 욕구가 높은 편입니다. 호기심이 많고, 변화와 모험을 추구하는 성향이 있습니다.");

        put("HA", "위험 회피 경향이 낮아, 불확실한 상황에도 비교적 편안하게 대처하는 편입니다. 새로운 상황을 두려워하기보다 받아들이는 경향이 있습니다.",
            "위험 회피 수준이 평균 범위입니다. 적당한 수준의 예방적 사고와 함께 새로운 것에 대한 개방성도 보입니다.",
            "잘못될 가능성이나 불안을 미리 많이 생각하는 편입니다. 신중하고 예방적이며, 안전을 중시하는 성향이 있습니다.");

        put("RD", "사회적 보상이나 타인의 반응에 덜 의존하는 편입니다. 독립적이며, 혼자서도 만족감을 느끼는 경향이 있습니다.",
            "보상 의존 수준이 평균 범위입니다. 타인의 인정과 관계를 중요하게 생각하면서도 적당한 거리감을 유지하는 편입니다.",
            "다른 사람의 칭찬, 관심, 따뜻한 관계에 민감하고 중요하게 여기는 편입니다. 관계에서 오는 보상에 동기 부여를 잘 받습니다.");

        put("P", "목표를 오래 지속하기보다는 상황에 따라 유연하게 바꾸는 편일 수 있습니다. 어려움이 있으면 방향을 전환하는 경향이 있습니다.",
            "인내력이 평균 범위입니다. 어려움이 있어도 어느 정도 끈기를 보이면서, 필요 시 유연하게 대처하는 편입니다.",
            "한번 정한 목표를 끝까지 지키려는 경향이 있습니다. 어려움 속에서도 끈기와 완성도를 중시하는 성향이 있습니다.");

        put("SD", "자기 주도성이나 목표 의식이 상대적으로 낮을 수 있습니다. 스스로 결정하고 책임지는 것보다 주변에 의존하는 경향이 있을 수 있습니다.",
            "자율성과 자기 지향 수준이 평균 범위입니다. 스스로 목표를 세우고 책임지려 하면서도, 필요 시 도움을 받는 편입니다.",
            "자기 목표가 분명하고, 스스로 결정하고 책임지는 편입니다. 자기 통제와 자기 수용 수준이 높은 편으로 해석됩니다.");

        put("C", "협동과 배려보다는 자기 중심적이거나 거리감이 있는 편일 수 있습니다. 타인과의 조화보다 개인의 입장을 우선할 수 있습니다.",
            "협동성 수준이 평균 범위입니다. 타인을 배려하고 협력하면서도, 자신의 선을 지키는 균형을 유지하는 편입니다.",
            "타인을 이해하고 배려하며, 협력과 공정함을 중요하게 여기는 편입니다. 연대감과 이타적 성향이 높은 편으로 해석됩니다.");

        put("ST", "초월적 가치나 영적·경이로운 경험에 덜 열려 있는 편입니다. 일상적·현실적 관점을 선호할 수 있습니다.",
            "자기 초월 수준이 평균 범위입니다. 삶의 의미나 더 큰 무엇인가에 대한 관심을 적당히 가지는 편입니다.",
            "삶의 의미, 자연·예술에 대한 감동, 영적이거나 초월적인 경험에 열려 있는 편입니다. 경이로움과 일체감을 느끼는 경향이 있습니다.");
    }

    private static void put(String scale, String low, String average, String high) {
        Map<String, String> band = new LinkedHashMap<>();
        band.put("low", low);
        band.put("average", average);
        band.put("high", high);
        TEXTS.put(scale, band);
    }

    /**
     * T점수에 따른 구간(낮음/평균/높음)의 해석 문구를 반환합니다.
     * @param scaleCode 척도 코드 (NS, HA, RD, P, SD, C, ST)
     * @param tScore T점수 (null이면 빈 문자열)
     */
    public static String interpret(String scaleCode, Double tScore) {
        if (tScore == null) return "";
        Map<String, String> band = TEXTS.get(scaleCode);
        if (band == null) return "";

        if (tScore < BAND_LOW_MAX) return band.get("low");
        if (tScore > BAND_HIGH_MIN) return band.get("high");
        return band.get("average");
    }

    /**
     * 모든 척도에 대해 T점수 맵을 기준으로 해석 맵을 생성합니다.
     */
    public static Map<String, String> interpretAll(Map<String, Double> scaleTScores) {
        Map<String, String> out = new LinkedHashMap<>();
        for (String code : TciScaleLabels.SCALE_ORDER) {
            String text = interpret(code, scaleTScores != null ? scaleTScores.get(code) : null);
            if (!text.isEmpty()) out.put(code, text);
        }
        return out;
    }

    /** 관리자용: T점수 구간 및 척도별 해석 문구 */
    public static String getReferenceDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("T점수 구간: <43 낮음, 43~57 평균, >57 높음 (척도별 적용)\n");
        for (String code : TciScaleLabels.SCALE_ORDER) {
            Map<String, String> band = TEXTS.get(code);
            if (band == null) continue;
            String name = TciScaleLabels.getDisplayName(code);
            sb.append("\n· ").append(code).append(" (").append(name).append(")\n");
            sb.append("  낮음: ").append(band.get("low")).append("\n");
            sb.append("  평균: ").append(band.get("average")).append("\n");
            sb.append("  높음: ").append(band.get("high")).append("\n");
        }
        return sb.toString();
    }

    private TciScaleInterpretation() {}
}
