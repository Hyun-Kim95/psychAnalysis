package com.tst.psychAnalysis.response;

import java.util.List;

/** NEO 등 척도별 점수 표시 시 주척도 그룹 헤더용 (예: N (신경증) 아래 N1, N2, ...) */
public record ScaleGroupDto(String groupLabel, List<String> scaleCodes) {}
