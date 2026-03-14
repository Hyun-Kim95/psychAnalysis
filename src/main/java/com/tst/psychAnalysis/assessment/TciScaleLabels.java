package com.tst.psychAnalysis.assessment;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TCI 검사 엑셀(9932A73F5D23FA661B.xlsx) 기준 척도 한글명 및 표시 순서.
 */
public final class TciScaleLabels {

    /** 표시 순서: NS, HA, RD, P, SD, C, ST */
    public static final List<String> SCALE_ORDER = List.of("NS", "HA", "RD", "P", "SD", "C", "ST");

    /** 척도 코드 → 한글명 (엑셀 시트1 대척도명) */
    private static final Map<String, String> LABELS = new LinkedHashMap<>();
    static {
        LABELS.put("NS", "자극추구");
        LABELS.put("HA", "위험회피");
        LABELS.put("RD", "사회적 민감성");
        LABELS.put("P", "인내력");
        LABELS.put("SD", "자율성");
        LABELS.put("C", "연대감");
        LABELS.put("ST", "자기초월");
    }

    public static Map<String, String> getDisplayNames() {
        return new LinkedHashMap<>(LABELS);
    }

    public static String getDisplayName(String scaleCode) {
        return LABELS.getOrDefault(scaleCode, scaleCode);
    }

    private TciScaleLabels() {}
}
