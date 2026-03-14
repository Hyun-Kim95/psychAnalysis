package com.tst.psychAnalysis.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccessLogCountRepository extends JpaRepository<AccessLogCount, Long> {

    Optional<AccessLogCount> findByClientIpMaskedAndLogDateAndEventType(
            String clientIpMasked, LocalDate logDate, String eventType);

    List<AccessLogCount> findByLogDateBetweenOrderByLogDateDescClientIpMaskedAscEventTypeAsc(
            LocalDate from, LocalDate to);
}
