package com.tst.psychAnalysis.admin;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class AccessLogService {

    private final AccessLogCountRepository accessLogCountRepository;

    public AccessLogService(AccessLogCountRepository accessLogCountRepository) {
        this.accessLogCountRepository = accessLogCountRepository;
    }

    @Transactional
    public void log(HttpServletRequest request, String eventType, UUID responseSessionId) {
        String ip = maskIp(request.getRemoteAddr());
        if (ip == null) return;
        LocalDate today = LocalDate.now();
        accessLogCountRepository.findByClientIpMaskedAndLogDateAndEventType(ip, today, eventType)
                .ifPresentOrElse(
                        row -> {
                            row.setCount(row.getCount() + 1);
                            accessLogCountRepository.save(row);
                        },
                        () -> {
                            AccessLogCount row = new AccessLogCount();
                            row.setClientIpMasked(ip);
                            row.setLogDate(today);
                            row.setEventType(eventType);
                            row.setCount(1L);
                            accessLogCountRepository.save(row);
                        }
                );
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

