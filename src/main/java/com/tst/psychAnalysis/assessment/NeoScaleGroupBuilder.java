package com.tst.psychAnalysis.assessment;

import com.tst.psychAnalysis.response.ScaleGroupDto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * NEO 검사에서 하위척도가 있을 때 주척도(N·E·O·A·C)별 그룹을 만듭니다.
 * API·PDF·관리자 기준 화면에서 동일한 규칙을 쓰기 위해 한곳에 둡니다.
 */
public final class NeoScaleGroupBuilder {

    private NeoScaleGroupBuilder() {}

    public static List<ScaleGroupDto> buildGroupsIfNeo(String assessmentName, List<String> scaleOrder, boolean english) {
        if (assessmentName == null || !assessmentName.contains("NEO") || scaleOrder == null || scaleOrder.isEmpty()) {
            return List.of();
        }
        boolean hasFacets = scaleOrder.stream().anyMatch(NeoScaleInterpretation.FACET_ORDER::contains);
        if (!hasFacets) {
            return List.of();
        }

        Map<String, List<String>> byFactor = new LinkedHashMap<>();
        for (String code : scaleOrder) {
            if (code == null || code.length() < 2) {
                continue;
            }
            String factor = code.substring(0, 1);
            if (NeoScaleInterpretation.MAIN_FACTOR_NAMES.containsKey(factor)) {
                byFactor.computeIfAbsent(factor, k -> new ArrayList<>()).add(code);
            }
        }
        List<ScaleGroupDto> groups = new ArrayList<>();
        for (String f : List.of("N", "E", "O", "A", "C")) {
            if (byFactor.containsKey(f)) {
                String label = NeoScaleInterpretation.MAIN_FACTOR_NAMES.get(f);
                groups.add(new ScaleGroupDto(LocalizationTexts.groupLabel(f + " (" + label + ")", english), byFactor.get(f)));
            }
        }
        return groups;
    }
}
