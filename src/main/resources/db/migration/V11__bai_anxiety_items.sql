-- BAI 불안검사: 단일 척도, 9문항, 4점(0~3) - 프로토타입용

UPDATE assessment SET description = '불안 수준을 간이 측정하는 검사입니다. 9문항, 약 3~5분 소요.' WHERE name = 'BAI 불안검사';

INSERT INTO scale (assessment_id, code, name, description)
SELECT a.id, 'A', '불안(Anxiety)', '불안·긴장·신체 증상 등'
FROM assessment a
WHERE a.name = 'BAI 불안검사';

INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'BAI 불안검사' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사' LIMIT 1) AND code = 'A'), n, txt, TRUE, FALSE, 1.0, n
FROM (VALUES
  (1, '몸이 떨리거나 긴장된다.'),
  (2, '잘못될 것 같은 두려움이 든다.'),
  (3, '가슴이 두근거리거나 숨이 가쁘다.'),
  (4, '갑자기 불안해지거나 공포감이 든다.'),
  (5, '어떤 일이 닥칠지 불안해서 집중이 안 된다.'),
  (6, '머리가 어지럽거나 멍한 느낌이 든다.'),
  (7, '심장이 쿵쾅거리거나 숨이 막힌다.'),
  (8, '손발이 저리거나 땀이 난다.'),
  (9, '불안해서 잠들기 어렵거나 잠을 설친다.')
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
WHERE a.name = 'BAI 불안검사';

-- 규준: 9문항×0~3 = 0~27점, 평균 9, SD 6
INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd)
SELECT a.id, s.id, 'default', 9.0, 6.0
FROM assessment a
JOIN scale s ON s.assessment_id = a.id
WHERE a.name = 'BAI 불안검사';

INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd)
SELECT id, NULL, 'default', 9.0, 6.0
FROM assessment WHERE name = 'BAI 불안검사' LIMIT 1;
