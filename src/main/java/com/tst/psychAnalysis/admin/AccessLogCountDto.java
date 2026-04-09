package com.tst.psychAnalysis.admin;

import java.time.LocalDate;

/**
 * 관리자 액세스 로그 API 응답 (이벤트 유형은 요청 언어에 맞게 표시).
 */
public record AccessLogCountDto(
        Long id,
        String clientIpMasked,
        LocalDate logDate,
        String eventType,
        Long count
) {}
