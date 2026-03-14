package com.tst.psychAnalysis.admin;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "access_log_count",
       uniqueConstraints = @UniqueConstraint(columnNames = {"client_ip_masked", "log_date", "event_type"}))
public class AccessLogCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_ip_masked", nullable = false, length = 64)
    private String clientIpMasked;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "count", nullable = false)
    private Long count = 1L;

    public Long getId() {
        return id;
    }

    public String getClientIpMasked() {
        return clientIpMasked;
    }

    public void setClientIpMasked(String clientIpMasked) {
        this.clientIpMasked = clientIpMasked;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
