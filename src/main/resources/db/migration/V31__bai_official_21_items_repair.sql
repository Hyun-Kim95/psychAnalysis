-- BAI 공식 21문항·선택지 보정 (V30 미적용 시 적용, 이미 적용됐으면 중복 없이 반영)

-- 1) 선택지 문구 공식 4점으로 통일 (assessment id로 직접 지정)
UPDATE choice SET label = CASE value
  WHEN 0 THEN '전혀 느끼지 않았다'
  WHEN 1 THEN '조금 느꼈다'
  WHEN 2 THEN '상당히 느꼈다'
  WHEN 3 THEN '심하게 느꼈다'
  ELSE label END
WHERE item_id IN (
  SELECT i.id FROM item i
  WHERE i.assessment_id IN (SELECT id FROM assessment WHERE name = 'BAI 불안검사' OR name = 'BAI 불안검사 (상세)')
);

-- 2) BAI 불안검사: 문항 1~9 텍스트 공식 문장으로
UPDATE item SET "text" = '가끔씩 몸이 저리고 쑤시며 감각이 마비된 느낌을 받는다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사' LIMIT 1) AND item_number = 1;

UPDATE item SET "text" = '흥분된 느낌을 받는다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사' LIMIT 1) AND item_number = 2;

UPDATE item SET "text" = '가끔씩 다리가 떨리곤 한다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사' LIMIT 1) AND item_number = 3;

UPDATE item SET "text" = '편안하게 쉴 수가 없다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사' LIMIT 1) AND item_number = 4;

UPDATE item SET "text" = '매우 나쁜 일이 일어날 것 같은 두려움을 느낀다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사' LIMIT 1) AND item_number = 5;

UPDATE item SET "text" = '어지러움(현기증)을 느낀다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사' LIMIT 1) AND item_number = 6;

UPDATE item SET "text" = '가끔씩 심장이 두근거리고 빨리 뛴다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사' LIMIT 1) AND item_number = 7;

UPDATE item SET "text" = '침착하지 못한다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사' LIMIT 1) AND item_number = 8;

UPDATE item SET "text" = '자주 겁을 먹고 무서움을 느낀다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사' LIMIT 1) AND item_number = 9;

-- 3) BAI 불안검사: 10~21문항 없으면 추가 (한 번만 삽입)
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT a.id, s.id, v.n, v.txt, TRUE, FALSE, 1.0, v.n
FROM assessment a
JOIN scale s ON s.assessment_id = a.id AND s.code = 'A'
CROSS JOIN (VALUES
  (10, '신경이 과민 되어 왔다.'),
  (11, '가끔씩 숨이 막히고 질식할 것 같다.'),
  (12, '자주 손이 떨린다.'),
  (13, '안절부절못해 한다.'),
  (14, '미칠 것 같은 두려움을 느낀다.'),
  (15, '가끔씩 숨쉬기 곤란할 때가 있다.'),
  (16, '죽을 것 같은 두려움을 느낀다.'),
  (17, '불안한 상태에 있다.'),
  (18, '자주 소화가 잘 안되고 뱃속이 불편하다.'),
  (19, '가끔씩 기절할 것 같다.'),
  (20, '자주 얼굴이 붉어지곤 한다.'),
  (21, '땀을 많이 흘린다. (더위로 인한 경우는 제외)')
) AS v(n, txt)
WHERE a.name = 'BAI 불안검사'
  AND NOT EXISTS (SELECT 1 FROM item i WHERE i.assessment_id = a.id AND i.item_number = v.n);

-- 4) BAI 불안검사: 10~21문항에 선택지가 없으면 추가
INSERT INTO choice (item_id, label, value, sort_order)
SELECT i.id, c.label, c.value, c.ord
FROM item i
JOIN assessment a ON a.id = i.assessment_id
CROSS JOIN (VALUES (1,'전혀 느끼지 않았다',0),(2,'조금 느꼈다',1),(3,'상당히 느꼈다',2),(4,'심하게 느꼈다',3)) AS c(ord, label, value)
WHERE a.name = 'BAI 불안검사' AND i.item_number >= 10
  AND NOT EXISTS (SELECT 1 FROM choice ch WHERE ch.item_id = i.id);

-- 5) BAI 불안검사 (상세): 문항 1~18 공식 문장으로
UPDATE item SET "text" = '가끔씩 몸이 저리고 쑤시며 감각이 마비된 느낌을 받는다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1) AND item_number = 1;

UPDATE item SET "text" = '흥분된 느낌을 받는다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1) AND item_number = 2;

UPDATE item SET "text" = '가끔씩 다리가 떨리곤 한다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1) AND item_number = 3;

UPDATE item SET "text" = '편안하게 쉴 수가 없다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1) AND item_number = 4;

UPDATE item SET "text" = '매우 나쁜 일이 일어날 것 같은 두려움을 느낀다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1) AND item_number = 5;

UPDATE item SET "text" = '어지러움(현기증)을 느낀다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1) AND item_number = 6;

UPDATE item SET "text" = '가끔씩 심장이 두근거리고 빨리 뛴다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1) AND item_number = 7;

UPDATE item SET "text" = '침착하지 못한다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1) AND item_number = 8;

UPDATE item SET "text" = '자주 겁을 먹고 무서움을 느낀다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1) AND item_number = 9;

UPDATE item SET "text" = '신경이 과민 되어 왔다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1) AND item_number = 10;

UPDATE item SET "text" = '가끔씩 숨이 막히고 질식할 것 같다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1) AND item_number = 11;

UPDATE item SET "text" = '자주 손이 떨린다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1) AND item_number = 12;

UPDATE item SET "text" = '안절부절못해 한다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1) AND item_number = 13;

UPDATE item SET "text" = '미칠 것 같은 두려움을 느낀다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1) AND item_number = 14;

UPDATE item SET "text" = '가끔씩 숨쉬기 곤란할 때가 있다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1) AND item_number = 15;

UPDATE item SET "text" = '죽을 것 같은 두려움을 느낀다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1) AND item_number = 16;

UPDATE item SET "text" = '불안한 상태에 있다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1) AND item_number = 17;

UPDATE item SET "text" = '자주 소화가 잘 안되고 뱃속이 불편하다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1) AND item_number = 18;

-- 6) BAI 불안검사 (상세): 19~21문항 없으면 추가
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT a.id, s.id, v.n, v.txt, TRUE, FALSE, 1.0, v.n
FROM assessment a
JOIN scale s ON s.assessment_id = a.id AND s.code = 'A'
CROSS JOIN (VALUES
  (19, '가끔씩 기절할 것 같다.'),
  (20, '자주 얼굴이 붉어지곤 한다.'),
  (21, '땀을 많이 흘린다. (더위로 인한 경우는 제외)')
) AS v(n, txt)
WHERE a.name = 'BAI 불안검사 (상세)'
  AND NOT EXISTS (SELECT 1 FROM item i WHERE i.assessment_id = a.id AND i.item_number = v.n);

-- 7) BAI 불안검사 (상세): 19~21문항에 선택지가 없으면 추가
INSERT INTO choice (item_id, label, value, sort_order)
SELECT i.id, c.label, c.value, c.ord
FROM item i
JOIN assessment a ON a.id = i.assessment_id
CROSS JOIN (VALUES (1,'전혀 느끼지 않았다',0),(2,'조금 느꼈다',1),(3,'상당히 느꼈다',2),(4,'심하게 느꼈다',3)) AS c(ord, label, value)
WHERE a.name = 'BAI 불안검사 (상세)' AND i.item_number >= 19
  AND NOT EXISTS (SELECT 1 FROM choice ch WHERE ch.item_id = i.id);

-- 8) 규준·설명
UPDATE norm SET mean = 18.0, sd = 12.0
WHERE assessment_id IN (SELECT id FROM assessment WHERE name IN ('BAI 불안검사', 'BAI 불안검사 (상세)'));

UPDATE assessment SET description = 'Beck Anxiety Inventory(벡 불안척도). 21문항, 0~3점. 지난 한 주간 경험 기준. 약 5~7분.'
WHERE name IN ('BAI 불안검사', 'BAI 불안검사 (상세)');
