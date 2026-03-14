-- NEO 성격검사: 5요인(N, E, O, A, C) × 5문항 = 25문항 (프로토타입용 문항)

UPDATE assessment SET description = '성격 5요인(신경증, 외향성, 개방성, 친화성, 성실성)을 측정하는 검사입니다. 25문항, 약 5~7분 소요.' WHERE name = 'NEO 성격검사';

INSERT INTO scale (assessment_id, code, name, description)
SELECT a.id, v.code, v.name, v.desc_text
FROM assessment a
CROSS JOIN (VALUES
  ('N', '신경증(Neuroticism)', '정서적 불안정성·스트레스 민감성'),
  ('E', '외향성(Extraversion)', '사회성·활동성·긍정 정서'),
  ('O', '개방성(Openness)', '새로운 경험·상상력·미에 대한 개방'),
  ('A', '친화성(Agreeableness)', '협력·신뢰·배려'),
  ('C', '성실성(Conscientiousness)', '계획성·책임감·목표 지향')
) AS v(code, name, desc_text)
WHERE a.name = 'NEO 성격검사';

-- N: 신경증
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'N'), n, txt, TRUE, FALSE, 1.0, ord
FROM (VALUES (1,'걱정이나 불안이 자주 찾아옵니다.',1), (2,'스트레스를 받으면 쉽게 흔들립니다.',2), (3,'기분이 우울해질 때가 있습니다.',3), (4,'작은 일에도 쉽게 짜증이 납니다.',4), (5,'감정 기복이 있는 편입니다.',5)) AS v(n,txt,ord);

-- E: 외향성
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'E'), n, txt, TRUE, FALSE, 1.0, ord
FROM (VALUES (1,'모임이나 사람이 많은 자리가 편합니다.',6), (2,'먼저 말을 거는 편입니다.',7), (3,'에너지가 넘치는 편입니다.',8), (4,'다른 사람과 함께 있을 때 기분이 좋습니다.',9), (5,'활발하고 사교적인 편입니다.',10)) AS v(n,txt,ord);

-- O: 개방성
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'O'), n, txt, TRUE, FALSE, 1.0, ord
FROM (VALUES (1,'새로운 아이디어나 경험에 관심이 많습니다.',11), (2,'상상력이 풍부한 편입니다.',12), (3,'예술이나 자연을 감상하는 것을 좋아합니다.',13), (4,'다양한 관점에서 생각하려고 합니다.',14), (5,'호기심이 많고 배우려는 자세가 있습니다.',15)) AS v(n,txt,ord);

-- A: 친화성
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'A'), n, txt, TRUE, FALSE, 1.0, ord
FROM (VALUES (1,'다른 사람을 쉽게 믿는 편입니다.',16), (2,'협력하는 것을 좋아합니다.',17), (3,'타인의 입장을 이해하려고 노력합니다.',18), (4,'다툼보다는 조화를 추구합니다.',19), (5,'배려심이 있는 편입니다.',20)) AS v(n,txt,ord);

-- C: 성실성
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'C'), n, txt, TRUE, FALSE, 1.0, ord
FROM (VALUES (1,'해야 할 일을 미루지 않는 편입니다.',21), (2,'계획을 세우고 실행하는 편입니다.',22), (3,'꼼꼼하고 책임감이 있습니다.',23), (4,'목표를 향해 꾸준히 노력합니다.',24), (5,'정해진 규칙을 잘 지킵니다.',25)) AS v(n,txt,ord);

-- 5점 리커트 보기
INSERT INTO choice (item_id, label, value, sort_order)
SELECT i.id, c.label, c.value, c.ord
FROM item i
JOIN assessment a ON a.id = i.assessment_id
CROSS JOIN (VALUES (1, '전혀 그렇지 않다', 1), (2, '그렇지 않다', 2), (3, '보통이다', 3), (4, '그렇다', 4), (5, '매우 그렇다', 5)) AS c(ord, label, value)
WHERE a.name = 'NEO 성격검사';

-- 규준: 척도별 5문항(5~25점), 평균 15, SD 5 / 총점 25문항(25~125), 평균 75, SD 15
INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd)
SELECT a.id, s.id, 'default', 15.0, 5.0
FROM assessment a
JOIN scale s ON s.assessment_id = a.id
WHERE a.name = 'NEO 성격검사';

INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd)
SELECT id, NULL, 'default', 75.0, 15.0
FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1;
