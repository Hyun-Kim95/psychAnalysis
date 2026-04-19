package com.tst.psychAnalysis.common;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 등록 클라이언트 IP. 리버스 프록시 뒤에서는 {@code X-Forwarded-For} 첫 값을 사용합니다.
 * 운영에서는 {@code server.forward-headers-strategy} 등으로 신뢰 경계를 맞춰야 합니다.
 */
public final class ClientIpResolver {

    private static final int MAX_LEN = 45;

    private ClientIpResolver() {
    }

    public static String resolve(HttpServletRequest request) {
        if (request == null) {
            return "";
        }
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            String first = xff.split(",")[0].trim();
            if (!first.isEmpty()) {
                return truncate(first);
            }
        }
        String ip = request.getRemoteAddr();
        return truncate(ip == null ? "" : ip);
    }

    private static String truncate(String s) {
        if (s.length() <= MAX_LEN) {
            return s;
        }
        return s.substring(0, MAX_LEN);
    }
}
