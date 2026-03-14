package com.tst.psychAnalysis.assessment;

import java.util.Map;

/**
 * BAI 불안검사 총점(원점수) 구간에 따른 해석.
 * 구간: 0~7 경미, 8~15 경도, 16~25 중등도, 26 이상 중증.
 */
public final class BaiInterpretation {

    private static final String MINIMAL = "불안 증상이 거의 없거나 매우 경미한 수준으로 보입니다. 일상 생활에 큰 영향은 없는 편입니다.";
    private static final String MILD = "불안 증상이 경미한 수준으로 보입니다. 가끔 긴장이나 걱정이 있을 수 있으나, 일상 기능은 유지되는 편입니다. 스트레스 관리나 휴식을 해 보시고, 지속되면 전문가 상담을 고려해 보세요.";
    private static final String MODERATE = "불안 증상이 중등도 수준으로 보입니다. 신체적 긴장, 두려움, 집중 곤란 등이 있을 수 있습니다. 전문가(정신건강의학과·상담심리) 상담을 권합니다.";
    private static final String SEVERE = "불안 증상이 상당한 수준으로 보입니다. 일상 기능이나 대인관계에 어려움이 있을 수 있습니다. 전문가(정신건강의학과·상담심리) 상담을 적극 권합니다.";

    /**
     * 총 원점수에 따른 해석을 척도 코드 "A"로 반환합니다.
     */
    public static Map<String, String> interpret(Double totalRawScore) {
        String text = interpretOne(totalRawScore);
        return text.isEmpty() ? Map.of() : Map.of("A", text);
    }

    public static String interpretOne(Double totalRawScore) {
        if (totalRawScore == null || totalRawScore < 0) return "";
        int raw = totalRawScore.intValue();
        if (raw <= 7) return MINIMAL;
        if (raw <= 15) return MILD;
        if (raw <= 25) return MODERATE;
        return SEVERE;
    }

    /** 관리자용: 기준 구간 및 해석 문구 전체 */
    public static String getReferenceDescription() {
        return "총점 구간: 0~7 경미, 8~15 경도, 16~25 중등도, 26 이상 중증\n"
                + "· 0~7(경미): " + MINIMAL + "\n"
                + "· 8~15(경도): " + MILD + "\n"
                + "· 16~25(중등도): " + MODERATE + "\n"
                + "· 26+(중증): " + SEVERE;
    }

    private BaiInterpretation() {}
}
