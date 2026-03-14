-- 회복탄력성 검사: 단일 척도, 10문항, 5점 리커트 - 프로토타입용

UPDATE assessment SET description = '스트레스 대처 및 회복탄력성을 측정하는 검사입니다. 10문항, 약 3~5분 소요.' WHERE name = '회복탄력성 검사';

INSERT INTO scale (assessment_id, code, name, description)
SELECT a.id, 'R', '회복탄력성(Resilience)', '역경 대처·극복·적응력'
FROM assessment a
WHERE a.name = '회복탄력성 검사';

INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND code = 'R'), n, txt, TRUE, FALSE, 1.0, n
FROM (VALUES
  (1, '어려움이 있어도 잘 이겨내는 편이다.'),
  (2, '실패해도 다시 일어나서 시도한다.'),
  (3, '스트레스를 받아도 적응해 나간다.'),
  (4, '힘든 일이 있어도 희망을 잃지 않는다.'),
  (5, '문제가 생기면 해결 방법을 찾으려 한다.'),
  (6, '변화나 불확실한 상황에도 대처할 수 있다.'),
  (7, '도움을 받을 수 있는 사람이 있다고 느낀다.'),
  (8, '어려운 시기를 겪어도 결국 잘 되리라고 믿는다.'),
  (9, '역경을 통해 배우고 성장할 수 있다고 생각한다.'),
  (10, '일상에서 작은 성취나 감사함을 느낀다.')
) AS v(n, txt);

-- 5점 리커트 보기
INSERT INTO choice (item_id, label, value, sort_order)
SELECT i.id, c.label, c.value, c.ord
FROM item i
JOIN assessment a ON a.id = i.assessment_id
CROSS JOIN (VALUES
  (1, '전혀 그렇지 않다', 1),
  (2, '그렇지 않다', 2),
  (3, '보통이다', 3),
  (4, '그렇다', 4),
  (5, '매우 그렇다', 5)
) AS c(ord, label, value)
WHERE a.name = '회복탄력성 검사';

-- 규준: 10문항×1~5 = 10~50점, 평균 30, SD 8
INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd)
SELECT a.id, s.id, 'default', 30.0, 8.0
FROM assessment a
JOIN scale s ON s.assessment_id = a.id
WHERE a.name = '회복탄력성 검사';

INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd)
SELECT id, NULL, 'default', 30.0, 8.0
FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1;
