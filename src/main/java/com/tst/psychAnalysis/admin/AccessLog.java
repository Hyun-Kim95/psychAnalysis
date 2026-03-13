package com.tst.psychAnalysis.admin;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime occurredAt;

    private String url;

    private String userAgent;

    private String clientIpMasked;

    private String eventType;

    private UUID responseSessionId;

    public Long getId() {
        return id;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public String getUrl() {
        return url;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getClientIpMasked() {
        return clientIpMasked;
    }

    public String getEventType() {
        return eventType;
    }

    public UUID getResponseSessionId() {
        return responseSessionId;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setClientIpMasked(String clientIpMasked) {
        this.clientIpMasked = clientIpMasked;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setResponseSessionId(UUID responseSessionId) {
        this.responseSessionId = responseSessionId;
    }
}

