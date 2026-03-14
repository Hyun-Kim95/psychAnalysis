package com.tst.psychAnalysis.assessment;

import java.util.Map;

/**
 * BDI 우울검사 총점(원점수) 구간에 따른 해석.
 * 구간: 0~13 경미, 14~19 경도, 20~28 중등도, 29 이상 중증.
 */
public final class BdiInterpretation {

    private static final String MINIMAL = "우울 증상이 거의 없거나 매우 경미한 수준으로 보입니다. 일상 생활에 큰 영향은 없는 편입니다.";
    private static final String MILD = "우울 증상이 경미한 수준으로 보입니다. 기분 저하나 피로감이 있을 수 있으나, 일상 기능은 유지되는 편입니다. 관심사나 대인관계를 이어가 보시고, 지속되면 전문가 상담을 고려해 보세요.";
    private static final String MODERATE = "우울 증상이 중등도 수준으로 보입니다. 기분, 수면, 에너지, 흥미 등에 영향을 느낄 수 있습니다. 전문가(정신건강의학과·상담심리) 상담을 권합니다.";
    private static final String SEVERE = "우울 증상이 상당한 수준으로 보입니다. 일상 기능이나 대인관계에 어려움이 있을 수 있습니다. 전문가(정신건강의학과·상담심리) 상담을 적극 권합니다.";

    /**
     * 총 원점수에 따른 해석을 척도 코드 "D"로 반환합니다.
     */
    public static Map<String, String> interpret(Double totalRawScore) {
        String text = interpretOne(totalRawScore);
        return text.isEmpty() ? Map.of() : Map.of("D", text);
    }

    public static String interpretOne(Double totalRawScore) {
        if (totalRawScore == null || totalRawScore < 0) return "";
        int raw = totalRawScore.intValue();
        if (raw <= 13) return MINIMAL;
        if (raw <= 19) return MILD;
        if (raw <= 28) return MODERATE;
        return SEVERE;
    }

    /** 관리자용: 기준 구간 및 해석 문구 전체 */
    public static String getReferenceDescription() {
        return "총점 구간: 0~13 경미, 14~19 경도, 20~28 중등도, 29 이상 중증\n"
                + "· 0~13(경미): " + MINIMAL + "\n"
                + "· 14~19(경도): " + MILD + "\n"
                + "· 20~28(중등도): " + MODERATE + "\n"
                + "· 29+(중증): " + SEVERE;
    }

    private BdiInterpretation() {}
}
