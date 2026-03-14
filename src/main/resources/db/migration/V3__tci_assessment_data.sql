-- TCI(성격 및 기질 검사) 프로토타입 데이터
-- 기존 검사가 있으면 비활성화하고, TCI를 새 assessment로 추가 (id 충돌 방지)

UPDATE assessment SET is_active = FALSE WHERE is_active = TRUE;

INSERT INTO assessment (name, description, is_active) VALUES
('TCI 검사', '성격 및 기질 검사(Temperament and Character Inventory) 프로토타입입니다. 기질 4요인(새로움 추구, 위험 회피, 보상 의존, 지속성)과 성격 3요인(자기 지향성, 협동성, 자기 초월)을 측정합니다.', TRUE);

-- 방금 삽입한 TCI 검사 id 사용 (서브쿼리)
INSERT INTO scale (assessment_id, code, name, description)
SELECT a.id, v.code, v.name, v.desc_text
FROM assessment a
CROSS JOIN (VALUES
  ('NS', '새로움 추구(Novelty Seeking)', '새로운 것에 대한 탐구와 자극 추구 경향'),
  ('HA', '위험 회피(Harm Avoidance)', '위험·불안 회피 및 예방적 행동 경향'),
  ('RD', '보상 의존(Reward Dependence)', '사회적 보상과 인정에 대한 민감성'),
  ('P',  '지속성(Persistence)', '어려움 속에서도 목표 지속 경향'),
  ('SD', '자기 지향성(Self-Directedness)', '자율성과 목표 지향성'),
  ('C',  '협동성(Cooperativeness)', '협력과 배려 경향'),
  ('ST', '자기 초월(Self-Transcendence)', '초월적 가치와 영적 경험에 대한 개방성')
) AS v(code, name, desc_text)
WHERE a.name = 'TCI 검사' AND a.is_active = TRUE;

-- 척도별 5문항 (총 35문항) - assessment/scale 서브쿼리로 id 조회
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE),
       (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE) AND code = 'NS'),
       1, '새로운 경험을 할 때 설레고 기대가 됩니다.', TRUE, FALSE, 1.0, 1
UNION ALL SELECT (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE) AND code = 'NS'), 2, '익숙한 것보다 새로운 것을 시도하는 편입니다.', TRUE, FALSE, 1.0, 2
UNION ALL SELECT (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE) AND code = 'NS'), 3, '변화가 많을수록 생활이 더 재미있다고 느낍니다.', TRUE, FALSE, 1.0, 3
UNION ALL SELECT (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE) AND code = 'NS'), 4, '새로운 사람이나 장소를 만나는 것을 좋아합니다.', TRUE, FALSE, 1.0, 4
UNION ALL SELECT (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE) AND code = 'NS'), 5, '일상적인 루틴보다는 예측할 수 없는 일이 더 좋습니다.', TRUE, FALSE, 1.0, 5;
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE) AND code = 'HA'), n, txt, TRUE, FALSE, 1.0, ord FROM (VALUES (1,'잘못될 가능성을 미리 많이 생각하는 편입니다.',6), (2,'불확실한 상황에서는 불안을 느낍니다.',7), (3,'위험할 수 있는 상황을 피하려고 합니다.',8), (4,'실패할까 봐 도전을 망설일 때가 많습니다.',9), (5,'걱정이 많아서 마음이 편할 때가 적습니다.',10)) AS v(n,txt,ord);
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE) AND code = 'RD'), n, txt, TRUE, FALSE, 1.0, ord FROM (VALUES (1,'다른 사람의 칭찬이나 인정을 받으면 기분이 좋아집니다.',11), (2,'주변 사람들과 따뜻한 관계를 유지하는 것이 중요합니다.',12), (3,'거절당하거나 무시당하면 상처를 받습니다.',13), (4,'다른 사람의 관심과 지지가 동기 부여가 됩니다.',14), (5,'혼자보다는 함께 있을 때 더 만족스럽습니다.',15)) AS v(n,txt,ord);
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE) AND code = 'P'), n, txt, TRUE, FALSE, 1.0, ord FROM (VALUES (1,'어려움이 있어도 목표를 포기하지 않습니다.',16), (2,'힘든 일도 끝까지 해내는 편입니다.',17), (3,'장기적인 계획을 세우고 꾸준히 실천합니다.',18), (4,'좌절해도 다시 시도하는 편입니다.',19), (5,'노력이 필요한 일에 끈기를 보입니다.',20)) AS v(n,txt,ord);
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE) AND code = 'SD'), n, txt, TRUE, FALSE, 1.0, ord FROM (VALUES (1,'스스로 결정하고 행동하는 편입니다.',21), (2,'자신의 목표가 무엇인지 비교적 분명합니다.',22), (3,'책임져야 할 일을 스스로 해내려고 합니다.',23), (4,'실패해도 자신을 비난하기보다는 개선점을 찾습니다.',24), (5,'자기 통제가 잘 되는 편입니다.',25)) AS v(n,txt,ord);
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE) AND code = 'C'), n, txt, TRUE, FALSE, 1.0, ord FROM (VALUES (1,'다른 사람 입장을 이해하려고 노력합니다.',26), (2,'협력이 필요한 일에서 배려하는 편입니다.',27), (3,'갈등이 있어도 대화로 해결하려고 합니다.',28), (4,'다른 사람을 도와주는 것을 좋아합니다.',29), (5,'공정함과 나눔을 중요하게 생각합니다.',30)) AS v(n,txt,ord);
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE) AND code = 'ST'), n, txt, TRUE, FALSE, 1.0, ord FROM (VALUES (1,'자연이나 예술을 통해 깊은 감동을 받은 적이 있습니다.',31), (2,'삶의 의미나 보다 큰 무엇인가를 생각할 때가 있습니다.',32), (3,'나만의 신념이나 가치가 삶에 영향을 줍니다.',33), (4,'일상 속에서도 경이로움을 느낄 때가 있습니다.',34), (5,'영적이거나 초월적인 경험에 열려 있는 편입니다.',35)) AS v(n,txt,ord);

-- 5점 리커트 보기: TCI 검사의 모든 문항에 동일 보기
INSERT INTO choice (item_id, label, value, sort_order)
SELECT i.id, c.label, c.value, c.sort_order
FROM item i
JOIN assessment a ON a.id = i.assessment_id
CROSS JOIN (VALUES
  (1, '전혀 그렇지 않다', 1),
  (2, '그렇지 않다', 2),
  (3, '보통이다', 3),
  (4, '그렇다', 4),
  (5, '매우 그렇다', 5)
) AS c(sort_order, label, value)
WHERE a.name = 'TCI 검사';

-- 규준: 척도별(5문항×1~5점 → 평균 15, SD 5), 총점(35문항 → 평균 105, SD 20)
INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd)
SELECT a.id, s.id, 'default', 15.0, 5.0
FROM assessment a
JOIN scale s ON s.assessment_id = a.id
WHERE a.name = 'TCI 검사';

INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd)
SELECT id, NULL, 'default', 105.0, 20.0
FROM assessment WHERE name = 'TCI 검사' AND is_active = TRUE;
