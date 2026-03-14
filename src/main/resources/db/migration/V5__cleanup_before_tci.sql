-- TCI 검사로 전환하기 전의 모든 응답·결과 기록 삭제
-- (관리자 접속 로그는 유지, 응답 세션 참조만 해제)

UPDATE access_log SET response_session_id = NULL WHERE response_session_id IS NOT NULL;

DELETE FROM item_response;
DELETE FROM result;
DELETE FROM response_session;

-- TCI가 아닌 기존 검사(척도/문항/보기/규준) 삭제
DELETE FROM choice WHERE item_id IN (SELECT id FROM item WHERE assessment_id IN (SELECT id FROM assessment WHERE name != 'TCI 검사'));
DELETE FROM item WHERE assessment_id IN (SELECT id FROM assessment WHERE name != 'TCI 검사');
DELETE FROM norm WHERE assessment_id IN (SELECT id FROM assessment WHERE name != 'TCI 검사');
DELETE FROM scale WHERE assessment_id IN (SELECT id FROM assessment WHERE name != 'TCI 검사');
DELETE FROM assessment WHERE name != 'TCI 검사';
