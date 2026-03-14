-- 접속 로그 이벤트 타입을 한글로 통일 (기존 영문 행을 한글 행에 합산 후 삭제)
INSERT INTO access_log_count (client_ip_masked, log_date, event_type, count)
SELECT client_ip_masked, log_date, '검사 시작', count FROM access_log_count WHERE event_type = 'SESSION_CREATED'
ON CONFLICT (client_ip_masked, log_date, event_type) DO UPDATE SET count = access_log_count.count + EXCLUDED.count;
DELETE FROM access_log_count WHERE event_type = 'SESSION_CREATED';

INSERT INTO access_log_count (client_ip_masked, log_date, event_type, count)
SELECT client_ip_masked, log_date, '검사 제출', count FROM access_log_count WHERE event_type = 'SESSION_COMPLETED'
ON CONFLICT (client_ip_masked, log_date, event_type) DO UPDATE SET count = access_log_count.count + EXCLUDED.count;
DELETE FROM access_log_count WHERE event_type = 'SESSION_COMPLETED';
