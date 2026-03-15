package com.tst.psychAnalysis.assessment;

import java.util.Map;

/**
 * BDI 우울검사 총점(원점수) 구간에 따른 해석.
 * 공식 기준: 0~9 정상, 10~15 가벼운 우울, 16~25 중등도 우울, 26~63 심한 우울.
 */
public final class BdiInterpretation {

    private static final String NORMAL = "우울 증상이 거의 없거나 정상 범위로 보입니다. 일상 생활에 큰 영향은 없는 편입니다.";
    private static final String MILD = "가벼운 우울 수준으로 보입니다. 기분 저하나 피로감이 있을 수 있으나, 일상 기능은 유지되는 편입니다. 관심사나 대인관계를 이어가 보시고, 지속되면 전문가 상담을 고려해 보세요.";
    private static final String MODERATE = "중등도 우울 수준으로 보입니다. 기분, 수면, 에너지, 흥미 등에 영향을 느낄 수 있습니다. 전문가(정신건강의학과·상담심리) 상담을 권합니다.";
    private static final String SEVERE = "심한 우울 수준으로 보입니다. 일상 기능이나 대인관계에 어려움이 있을 수 있습니다. 전문가(정신건강의학과·상담심리) 상담을 적극 권합니다.";

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
        if (raw <= 9) return NORMAL;
        if (raw <= 15) return MILD;
        if (raw <= 25) return MODERATE;
        return SEVERE;
    }

    /** 관리자용: 기준 구간 및 해석 문구 전체 */
    public static String getReferenceDescription() {
        return "총점 구간(공식): 0~9 정상, 10~15 가벼운 우울, 16~25 중등도 우울, 26~63 심한 우울\n"
                + "· 0~9(정상): " + NORMAL + "\n"
                + "· 10~15(가벼운 우울): " + MILD + "\n"
                + "· 16~25(중등도 우울): " + MODERATE + "\n"
                + "· 26~63(심한 우울): " + SEVERE;
    }

    private BdiInterpretation() {}
}
