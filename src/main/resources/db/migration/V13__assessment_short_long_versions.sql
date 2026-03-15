-- 간단/상세 버전 구분: 기존 검사는 간단 버전으로 설명 보강, 상세 버전(문항 수 많은) 추가

UPDATE assessment SET description = '성격 및 기질 검사(TCI). 간단 버전 35문항, 약 7~10분.' WHERE name = 'TCI 검사';
UPDATE assessment SET description = '성격 5요인(NEO). 간단 버전 25문항, 약 5~7분.' WHERE name = 'NEO 성격검사';
UPDATE assessment SET description = '우울 간이 검사(BDI). 간단 버전 9문항, 약 3~5분.' WHERE name = 'BDI 우울검사';
UPDATE assessment SET description = '불안 간이 검사(BAI). 간단 버전 9문항, 약 3~5분.' WHERE name = 'BAI 불안검사';
UPDATE assessment SET description = '회복탄력성 검사. 간단 버전 10문항, 약 3~5분.' WHERE name = '회복탄력성 검사';

-- ========== TCI 검사 (상세) 70문항 ==========
INSERT INTO assessment (name, description, is_active) VALUES
('TCI 검사 (상세)', '성격 및 기질 검사(TCI) 상세 버전. 70문항, 약 15~20분.', TRUE);

INSERT INTO scale (assessment_id, code, name, description)
SELECT a.id, v.code, v.name, v.desc_text FROM assessment a
CROSS JOIN (VALUES
  ('NS', '새로움 추구', '새로운 것에 대한 탐구와 자극 추구 경향'),
  ('HA', '위험 회피', '위험·불안 회피 및 예방적 행동 경향'),
  ('RD', '보상 의존', '사회적 보상과 인정에 대한 민감성'),
  ('P', '지속성', '어려움 속에서도 목표 지속 경향'),
  ('SD', '자기 지향성', '자율성과 목표 지향성'),
  ('C', '협동성', '협력과 배려 경향'),
  ('ST', '자기 초월', '초월적 가치와 영적 경험에 대한 개방성')
) AS v(code, name, desc_text)
WHERE a.name = 'TCI 검사 (상세)';

-- TCI 상세: 척도별 10문항. NS
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'NS' LIMIT 1), ord, txt, TRUE, FALSE, 1.0, ord FROM (VALUES
(1,'새로운 경험을 할 때 설레고 기대가 됩니다.'),(2,'익숙한 것보다 새로운 것을 시도하는 편입니다.'),(3,'변화가 많을수록 생활이 더 재미있다고 느낍니다.'),(4,'새로운 사람이나 장소를 만나는 것을 좋아합니다.'),(5,'일상적인 루틴보다는 예측할 수 없는 일이 더 좋습니다.'),
(6,'새로운 것을 배우거나 시도하는 데 거리낌이 없습니다.'),(7,'반복되는 일상보다는 다양한 경험이 좋습니다.'),(8,'호기심이 많아 새로운 분야에 관심이 있습니다.'),(9,'변화를 두려워하기보다 받아들이는 편입니다.'),(10,'새로운 환경에 빠르게 적응하는 편입니다.')
) AS v(ord, txt);

-- HA 10문항
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'HA' LIMIT 1), ord, txt, TRUE, FALSE, 1.0, ord FROM (VALUES
(1,'잘못될 가능성을 미리 많이 생각하는 편입니다.'),(2,'불확실한 상황에서는 불안을 느낍니다.'),(3,'위험할 수 있는 상황을 피하려고 합니다.'),(4,'실패할까 봐 도전을 망설일 때가 많습니다.'),(5,'걱정이 많아서 마음이 편할 때가 적습니다.'),
(6,'결정을 내리기 전에 오래 고민합니다.'),(7,'새로운 일을 시작할 때 불안합니다.'),(8,'비판이나 거절을 받을까 걱정됩니다.'),(9,'몸이 아프지 않을까 자주 생각합니다.'),(10,'실수하지 않으려고 매우 조심합니다.')
) AS v(ord, txt);

-- RD 10문항
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'RD' LIMIT 1), ord, txt, TRUE, FALSE, 1.0, ord FROM (VALUES
(1,'다른 사람의 칭찬이나 인정을 받으면 기분이 좋아집니다.'),(2,'주변 사람들과 따뜻한 관계를 유지하는 것이 중요합니다.'),(3,'거절당하거나 무시당하면 상처를 받습니다.'),(4,'다른 사람의 관심과 지지가 동기 부여가 됩니다.'),(5,'혼자보다는 함께 있을 때 더 만족스럽습니다.'),
(6,'친한 사람과의 대화에서 위로를 받습니다.'),(7,'타인의 반응을 신경 쓰는 편입니다.'),(8,'소속감을 느끼는 모임이 있습니다.'),(9,'도움을 요청하는 것을 어렵지 않게 느낍니다.'),(10,'다른 사람이 나를 어떻게 생각하는지 중요합니다.')
) AS v(ord, txt);

-- P 10문항
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'P' LIMIT 1), ord, txt, TRUE, FALSE, 1.0, ord FROM (VALUES
(1,'어려움이 있어도 목표를 포기하지 않습니다.'),(2,'힘든 일도 끝까지 해내는 편입니다.'),(3,'장기적인 계획을 세우고 꾸준히 실천합니다.'),(4,'좌절해도 다시 시도하는 편입니다.'),(5,'노력이 필요한 일에 끈기를 보입니다.'),
(6,'목표가 정해지면 달성할 때까지 노력합니다.'),(7,'어려운 과제도 포기하지 않고 해냅니다.'),(8,'실패해도 원인을 찾아 다시 도전합니다.'),(9,'단기적 보상보다 장기 목표를 선택합니다.'),(10,'해야 할 일을 미루지 않고 처리합니다.')
) AS v(ord, txt);

-- SD 10문항
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'SD' LIMIT 1), ord, txt, TRUE, FALSE, 1.0, ord FROM (VALUES
(1,'스스로 결정하고 행동하는 편입니다.'),(2,'자신의 목표가 무엇인지 비교적 분명합니다.'),(3,'책임져야 할 일을 스스로 해내려고 합니다.'),(4,'실패해도 자신을 비난하기보다는 개선점을 찾습니다.'),(5,'자기 통제가 잘 되는 편입니다.'),
(6,'남의 눈보다 자신의 기준으로 판단합니다.'),(7,'하고 싶은 일을 스스로 정해 실천합니다.'),(8,'잘못했을 때 변명보다는 수정하려 합니다.'),(9,'시간과 계획을 스스로 관리하는 편입니다.'),(10,'자신에 대한 이해가 있는 편입니다.')
) AS v(ord, txt);

-- C 10문항
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1), ord, txt, TRUE, FALSE, 1.0, ord FROM (VALUES
(1,'다른 사람 입장을 이해하려고 노력합니다.'),(2,'협력이 필요한 일에서 배려하는 편입니다.'),(3,'갈등이 있어도 대화로 해결하려고 합니다.'),(4,'다른 사람을 도와주는 것을 좋아합니다.'),(5,'공정함과 나눔을 중요하게 생각합니다.'),
(6,'팀에서 내 역할을 다하려 합니다.'),(7,'다른 사람의 의견을 경청합니다.'),(8,'이기기보다 함께 좋은 결과를 원합니다.'),(9,'불의를 보면 참지 못하는 편입니다.'),(10,'약한 사람을 배려하는 편입니다.')
) AS v(ord, txt);

-- ST 10문항
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'ST' LIMIT 1), ord, txt, TRUE, FALSE, 1.0, ord FROM (VALUES
(1,'자연이나 예술을 통해 깊은 감동을 받은 적이 있습니다.'),(2,'삶의 의미나 보다 큰 무엇인가를 생각할 때가 있습니다.'),(3,'나만의 신념이나 가치가 삶에 영향을 줍니다.'),(4,'일상 속에서도 경이로움을 느낄 때가 있습니다.'),(5,'영적이거나 초월적인 경험에 열려 있는 편입니다.'),
(6,'예술 작품이나 자연 풍경에 감동합니다.'),(7,'삶의 목적에 대해 생각할 때가 있습니다.'),(8,'나만의 철학이나 가치관이 있습니다.'),(9,'작은 것에서 의미를 찾는 편입니다.'),(10,'물질보다 정신적 만족을 더 중요하게 여깁니다.')
) AS v(ord, txt);

-- TCI 상세 보기 + 규준
INSERT INTO choice (item_id, label, value, sort_order)
SELECT i.id, c.label, c.value, c.ord FROM item i
JOIN assessment a ON a.id = i.assessment_id
CROSS JOIN (VALUES (1,'전혀 그렇지 않다',1),(2,'그렇지 않다',2),(3,'보통이다',3),(4,'그렇다',4),(5,'매우 그렇다',5)) AS c(ord, label, value)
WHERE a.name = 'TCI 검사 (상세)';

INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd)
SELECT a.id, s.id, 'default', 30.0, 10.0 FROM assessment a JOIN scale s ON s.assessment_id = a.id WHERE a.name = 'TCI 검사 (상세)';
INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd)
SELECT id, NULL, 'default', 210.0, 35.0 FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1;

-- ========== NEO 성격검사 (상세) 50문항 ==========
INSERT INTO assessment (name, description, is_active) VALUES
('NEO 성격검사 (상세)', '성격 5요인(NEO) 상세 버전. 50문항, 약 10~15분.', TRUE);

INSERT INTO scale (assessment_id, code, name, description)
SELECT a.id, v.code, v.name, v.desc_text FROM assessment a
CROSS JOIN (VALUES ('N','신경증','정서적 불안정성'),('E','외향성','사회성·활동성'),('O','개방성','새 경험·상상력'),('A','친화성','협력·배려'),('C','성실성','계획성·책임감')) AS v(code, name, desc_text)
WHERE a.name = 'NEO 성격검사 (상세)';

INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'N' LIMIT 1), rn, txt, TRUE, FALSE, 1.0, rn
FROM (VALUES (1,'걱정이나 불안이 자주 찾아옵니다.'),(2,'스트레스를 받으면 쉽게 흔들립니다.'),(3,'기분이 우울해질 때가 있습니다.'),(4,'작은 일에도 쉽게 짜증이 납니다.'),(5,'감정 기복이 있는 편입니다.'),(6,'불안해지면 잠을 설칩니다.'),(7,'비판에 민감한 편입니다.'),(8,'걱정이 많아 마음이 무겁습니다.'),(9,'기분 전환이 어려운 편입니다.'),(10,'자신이 불리할까 봐 걱정합니다.')) AS v(rn, txt);

INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'E' LIMIT 1), rn, txt, TRUE, FALSE, 1.0, rn
FROM (VALUES (1,'모임이나 사람이 많은 자리가 편합니다.'),(2,'먼저 말을 거는 편입니다.'),(3,'에너지가 넘치는 편입니다.'),(4,'다른 사람과 함께 있을 때 기분이 좋습니다.'),(5,'활발하고 사교적인 편입니다.'),(6,'대화를 이끌어 가는 편입니다.'),(7,'외로운 시간보다 함께 있는 시간을 선호합니다.'),(8,'낯선 사람과도 쉽게 친해집니다.'),(9,'재미있는 일에 적극 참여합니다.'),(10,'사람들 앞에서 말하는 것이 편합니다.')) AS v(rn, txt);

INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'O' LIMIT 1), rn, txt, TRUE, FALSE, 1.0, rn
FROM (VALUES (1,'새로운 아이디어나 경험에 관심이 많습니다.'),(2,'상상력이 풍부한 편입니다.'),(3,'예술이나 자연을 감상하는 것을 좋아합니다.'),(4,'다양한 관점에서 생각하려고 합니다.'),(5,'호기심이 많고 배우려는 자세가 있습니다.'),(6,'추상적이거나 철학적인 주제에 관심이 있습니다.'),(7,'새로운 음식이나 문화를 경험하는 것을 좋아합니다.'),(8,'창의적인 활동을 즐깁니다.'),(9,'일상과 다른 경험에 열려 있습니다.'),(10,'이상과 가치에 대해 생각할 때가 있습니다.')) AS v(rn, txt);

INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'A' LIMIT 1), rn, txt, TRUE, FALSE, 1.0, rn
FROM (VALUES (1,'다른 사람을 쉽게 믿는 편입니다.'),(2,'협력하는 것을 좋아합니다.'),(3,'타인의 입장을 이해하려고 노력합니다.'),(4,'다툼보다는 조화를 추구합니다.'),(5,'배려심이 있는 편입니다.'),(6,'남을 비난하기보다 받아들이는 편입니다.'),(7,'다른 사람의 성공을 기뻐합니다.'),(8,'정직하고 공정하게 행동하려 합니다.'),(9,'경쟁보다 협력을 선택합니다.'),(10,'다른 사람의 감정을 읽는 편입니다.')) AS v(rn, txt);

INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1), rn, txt, TRUE, FALSE, 1.0, rn
FROM (VALUES (1,'해야 할 일을 미루지 않는 편입니다.'),(2,'계획을 세우고 실행하는 편입니다.'),(3,'꼼꼼하고 책임감이 있습니다.'),(4,'목표를 향해 꾸준히 노력합니다.'),(5,'정해진 규칙을 잘 지킵니다.'),(6,'일을 끝까지 마무리합니다.'),(7,'시간을 계획적으로 사용합니다.'),(8,'세부 사항을 놓치지 않습니다.'),(9,'목표 달성을 위해 노력합니다.'),(10,'약속을 지키는 편입니다.')) AS v(rn, txt);

INSERT INTO choice (item_id, label, value, sort_order)
SELECT i.id, c.label, c.value, c.ord FROM item i JOIN assessment a ON a.id = i.assessment_id
CROSS JOIN (VALUES (1,'전혀 그렇지 않다',1),(2,'그렇지 않다',2),(3,'보통이다',3),(4,'그렇다',4),(5,'매우 그렇다',5)) AS c(ord, label, value)
WHERE a.name = 'NEO 성격검사 (상세)';

INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd)
SELECT a.id, s.id, 'default', 30.0, 10.0 FROM assessment a JOIN scale s ON s.assessment_id = a.id WHERE a.name = 'NEO 성격검사 (상세)';
INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd)
SELECT id, NULL, 'default', 150.0, 30.0 FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1;

-- ========== BDI 우울검사 (상세) 18문항 ==========
INSERT INTO assessment (name, description, is_active) VALUES ('BDI 우울검사 (상세)', '우울 간이 검사(BDI) 상세 버전. 18문항, 약 5~8분.', TRUE);
INSERT INTO scale (assessment_id, code, name, description)
SELECT id, 'D', '우울', '우울 기분·동기 등' FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1;
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1), rn, txt, TRUE, FALSE, 1.0, rn FROM (VALUES
(1,'기분이 가라앉거나 우울한 느낌이 든다.'),(2,'하고 싶은 일에 대한 흥미나 재미가 줄었다.'),(3,'잠들기 어렵거나 자주 깬다.'),(4,'피곤하거나 기운이 없다.'),(5,'먹는 맛이 없거나 과하게 먹는다.'),(6,'자신이 나쁘거나 실패자라고 느낀다.'),(7,'일에나 일상에 집중하기 어렵다.'),(8,'말이나 움직임이 느려지거나 반대로 초조하다.'),(9,'차라리 없었으면 좋겠다는 생각이 든다.'),
(10,'미래에 대한 희망이 없다고 느낀다.'),(11,'의욕이 잘 생기지 않는다.'),(12,'작은 일도 부담스럽게 느껴진다.'),(13,'불면증이나 과도한 수면이 있다.'),(14,'자신을 비난하거나 후회가 많다.'),(15,'의사결정이 어렵다.'),(16,'가치 없다고 느낀다.'),(17,'에너지가 없고 무기력하다.'),(18,'일상생활이 힘들다.')
) AS v(rn, txt);
INSERT INTO choice (item_id, label, value, sort_order)
SELECT i.id, c.label, c.value, c.ord FROM item i JOIN assessment a ON a.id = i.assessment_id
CROSS JOIN (VALUES (1,'전혀 없음',0),(2,'가끔',1),(3,'자주',2),(4,'거의 항상',3)) AS c(ord, label, value) WHERE a.name = 'BDI 우울검사 (상세)';
INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd) SELECT a.id, s.id, 'default', 18.0, 12.0 FROM assessment a JOIN scale s ON s.assessment_id = a.id WHERE a.name = 'BDI 우울검사 (상세)';
INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd) SELECT id, NULL, 'default', 18.0, 12.0 FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1;

-- ========== BAI 불안검사 (상세) 18문항 ==========
INSERT INTO assessment (name, description, is_active) VALUES ('BAI 불안검사 (상세)', '불안 간이 검사(BAI) 상세 버전. 18문항, 약 5~8분.', TRUE);
INSERT INTO scale (assessment_id, code, name, description)
SELECT id, 'A', '불안', '불안·긴장·신체 증상' FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1;
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1) AND code = 'A' LIMIT 1), rn, txt, TRUE, FALSE, 1.0, rn FROM (VALUES
(1,'몸이 떨리거나 긴장된다.'),(2,'잘못될 것 같은 두려움이 든다.'),(3,'가슴이 두근거리거나 숨이 가쁘다.'),(4,'갑자기 불안해지거나 공포감이 든다.'),(5,'어떤 일이 닥칠지 불안해서 집중이 안 된다.'),(6,'머리가 어지럽거나 멍한 느낌이 든다.'),(7,'심장이 쿵쾅거리거나 숨이 막힌다.'),(8,'손발이 저리거나 땀이 난다.'),(9,'불안해서 잠들기 어렵거나 잠을 설친다.'),
(10,'신경이 예민해져서 불안하다.'),(11,'숨이 막히거나 헐떡거린다.'),(12,'손이나 몸이 떨린다.'),(13,'어디선가 불안한 느낌이 든다.'),(14,'실수할까 봐 불안하다.'),(15,'가슴이 조여오는 느낌이 든다.'),(16,'공황처럼 느껴질 때가 있다.'),(17,'몸이 굳거나 뻣뻣해진다.'),(18,'불안으로 인해 일상이 방해받는다.')
) AS v(rn, txt);
INSERT INTO choice (item_id, label, value, sort_order)
SELECT i.id, c.label, c.value, c.ord FROM item i JOIN assessment a ON a.id = i.assessment_id
CROSS JOIN (VALUES (1,'전혀 없음',0),(2,'가끔',1),(3,'자주',2),(4,'거의 항상',3)) AS c(ord, label, value) WHERE a.name = 'BAI 불안검사 (상세)';
INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd) SELECT a.id, s.id, 'default', 18.0, 12.0 FROM assessment a JOIN scale s ON s.assessment_id = a.id WHERE a.name = 'BAI 불안검사 (상세)';
INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd) SELECT id, NULL, 'default', 18.0, 12.0 FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1;

-- ========== 회복탄력성 검사 (상세) 20문항 ==========
INSERT INTO assessment (name, description, is_active) VALUES ('회복탄력성 검사 (상세)', '회복탄력성 상세 버전. 20문항, 약 5~8분.', TRUE);
INSERT INTO scale (assessment_id, code, name, description)
SELECT id, 'R', '회복탄력성', '역경 대처·극복·적응력' FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1;
INSERT INTO item (assessment_id, scale_id, item_number, "text", is_required, is_reverse_scored, weight, sort_order)
SELECT (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1), (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1), rn, txt, TRUE, FALSE, 1.0, rn FROM (VALUES
(1,'어려움이 있어도 잘 이겨내는 편이다.'),(2,'실패해도 다시 일어나서 시도한다.'),(3,'스트레스를 받아도 적응해 나간다.'),(4,'힘든 일이 있어도 희망을 잃지 않는다.'),(5,'문제가 생기면 해결 방법을 찾으려 한다.'),(6,'변화나 불확실한 상황에도 대처할 수 있다.'),(7,'도움을 받을 수 있는 사람이 있다고 느낀다.'),(8,'어려운 시기를 겪어도 결국 잘 되리라고 믿는다.'),(9,'역경을 통해 배우고 성장할 수 있다고 생각한다.'),(10,'일상에서 작은 성취나 감사함을 느낀다.'),
(11,'좌절해도 금방 회복하는 편이다.'),(12,'어려운 결정을 내릴 수 있다.'),(13,'스트레스 상황에서도 침착함을 유지한다.'),(14,'목표가 무너져도 다시 세워 나간다.'),(15,'주변 사람에게 의지할 수 있다고 느낀다.'),(16,'과거의 어려움을 극복한 경험이 있다.'),(17,'새로운 상황에 적응하는 데 시간이 오래 걸리지 않는다.'),(18,'자신이 해낼 수 있다는 믿음이 있다.'),(19,'어려움 속에서도 의미를 찾으려 한다.'),(20,'삶의 전반에 만족감을 느낀다.')
) AS v(rn, txt);
INSERT INTO choice (item_id, label, value, sort_order)
SELECT i.id, c.label, c.value, c.ord FROM item i JOIN assessment a ON a.id = i.assessment_id
CROSS JOIN (VALUES (1,'전혀 그렇지 않다',1),(2,'그렇지 않다',2),(3,'보통이다',3),(4,'그렇다',4),(5,'매우 그렇다',5)) AS c(ord, label, value) WHERE a.name = '회복탄력성 검사 (상세)';
INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd) SELECT a.id, s.id, 'default', 60.0, 15.0 FROM assessment a JOIN scale s ON s.assessment_id = a.id WHERE a.name = '회복탄력성 검사 (상세)';
INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd) SELECT id, NULL, 'default', 60.0, 15.0 FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1;
