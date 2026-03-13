package com.tst.psychAnalysis.admin;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AccessLogService {

    private final AccessLogRepository accessLogRepository;

    public AccessLogService(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    public void log(HttpServletRequest request, String eventType, UUID responseSessionId) {
        AccessLog log = new AccessLog();
        log.setOccurredAt(LocalDateTime.now());
        log.setUrl(request.getRequestURI());
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setClientIpMasked(maskIp(request.getRemoteAddr()));
        log.setEventType(eventType);
        log.setResponseSessionId(responseSessionId);
        accessLogRepository.save(log);
    }

    private String maskIp(String ip) {
        if (ip == null) {
            return null;
        }
        int lastDot = ip.lastIndexOf('.');
        if (lastDot > 0) {
            return ip.substring(0, lastDot) + ".xxx";
        }
        return ip;
    }
}

