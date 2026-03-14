-- 접속 로그를 건별이 아닌 IP·날짜·이벤트별 횟수로 저장

DROP TABLE IF EXISTS access_log;

CREATE TABLE access_log_count (
    id                  BIGSERIAL PRIMARY KEY,
    client_ip_masked    VARCHAR(64) NOT NULL,
    log_date            DATE NOT NULL,
    event_type          VARCHAR(50) NOT NULL,
    count               BIGINT NOT NULL DEFAULT 1,
    UNIQUE (client_ip_masked, log_date, event_type)
);

CREATE INDEX idx_access_log_count_date ON access_log_count (log_date);
CREATE INDEX idx_access_log_count_event ON access_log_count (event_type);
