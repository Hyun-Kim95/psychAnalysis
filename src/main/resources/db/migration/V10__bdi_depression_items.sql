-- BDI 우울검사: 단일 척도, 9문항, 4점(0~3) - 프로토타입용

UPDATE assessment SET description = '우울 수준을 간이 측정하는 검사입니다. 9문항, 약 3~5분 소요.' WHERE name = 'BDI 우울검사';

INSERT INTO scale (assessment_id, code, name, description)
SELECT a.id, 'D', '우울(Depression)', '우울 기분·동기·생활 만족 등'
FROM assessment a
WHERE a.name = 'BDI 우울검사';

INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D'), n, txt, TRUE, FALSE, 1.0, n
FROM (VALUES
  (1, '기분이 가라앉거나 우울한 느낌이 든다.'),
  (2, '하고 싶던 일에 대한 흥미나 재미가 줄었다.'),
  (3, '잠들기 어렵거나 자주 깬다.'),
  (4, '피곤하거나 기운이 없다.'),
  (5, '먹는 맛이 없거나 과하게 먹는다.'),
  (6, '자신이 나쁘거나 실패자라고 느낀다.'),
  (7, '일에나 일상에 집중하기 어렵다.'),
  (8, '말이나 움직임이 느려지거나 반대로 초조하다.'),
  (9, '차라리 없었으면 좋겠다는 생각이 든다.')
) AS v(n, txt);

-- 4점 보기 (0~3)
INSERT INTO choice (item_id, label, value, sort_order)
SELECT i.id, c.label, c.value, c.ord
FROM item i
JOIN assessment a ON a.id = i.assessment_id
CROSS JOIN (VALUES
  (1, '전혀 없음', 0),
  (2, '가끔', 1),
  (3, '자주', 2),
  (4, '거의 항상', 3)
) AS c(ord, label, value)
WHERE a.name = 'BDI 우울검사';

-- 규준: 9문항×0~3 = 0~27점, 평균 9, SD 6
INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd)
SELECT a.id, s.id, 'default', 9.0, 6.0
FROM assessment a
JOIN scale s ON s.assessment_id = a.id
WHERE a.name = 'BDI 우울검사';

INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd)
SELECT id, NULL, 'default', 9.0, 6.0
FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1;
