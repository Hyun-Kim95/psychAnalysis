-- 문항 문장 자연스럽게 재적용 (JOIN으로 확실히 매칭, V21에서 반영 안 됐을 때 대비)

-- NEO 성격검사 (간단)
UPDATE item i SET "text" = '별일 없는데도 기분이 가라앉고, 하고 싶던 일이 잘 떠오르지 않을 때가 있습니다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'NEO 성격검사' AND s.code = 'N' AND i.item_number = 3;

UPDATE item i SET "text" = '친구나 동료와 시간을 보내면, 기분이 좋아지거나 회복된 느낌이 듭니다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'NEO 성격검사' AND s.code = 'E' AND i.item_number = 4;

-- NEO 성격검사 (상세)
UPDATE item i SET "text" = '별일 없는데도 기분이 가라앉고, 하고 싶던 일이 잘 떠오르지 않을 때가 있습니다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'NEO 성격검사 (상세)' AND s.code = 'N' AND i.item_number = 3;

UPDATE item i SET "text" = '걱정이 많아서 마음이 무거운 편입니다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'NEO 성격검사 (상세)' AND s.code = 'N' AND i.item_number = 8;

UPDATE item i SET "text" = '친구나 동료와 시간을 보내면, 기분이 좋아지거나 회복된 느낌이 듭니다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'NEO 성격검사 (상세)' AND s.code = 'E' AND i.item_number = 4;

UPDATE item i SET "text" = '이상, 가치, 삶의 방향에 대해 생각하는 편입니다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'NEO 성격검사 (상세)' AND s.code = 'O' AND i.item_number = 10;

-- TCI 검사 (간단)
UPDATE item i SET "text" = '중요한 결정을 할 때, 내가 원하는 것보다 주변 사람이 어떻게 생각할까를 먼저 떠올리는 편입니다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'TCI 검사' AND s.code = 'RD' AND i.item_number = 4;

UPDATE item i SET "text" = '자연이나 예술 작품을 볼 때, 말로 표현하기 어려운 경이로움이나 연결감을 느낍니다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'TCI 검사' AND s.code = 'ST' AND i.item_number = 1;

UPDATE item i SET "text" = '일상의 작은 순간들(대화, 풍경, 음악 등)이 이상하게도 감사하게 느껴집니다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'TCI 검사' AND s.code = 'ST' AND i.item_number = 5;

-- TCI 검사 (상세)
UPDATE item i SET "text" = '중요한 결정을 할 때, 내가 원하는 것보다 주변 사람이 어떻게 생각할까를 먼저 떠올리는 편입니다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'TCI 검사 (상세)' AND s.code = 'RD' AND i.item_number = 4;

UPDATE item i SET "text" = '상대방이 제 말이나 행동에 어떻게 반응했는지, 나중에도 자꾸 생각하는 편입니다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'TCI 검사 (상세)' AND s.code = 'RD' AND i.item_number = 7;

UPDATE item i SET "text" = '자연이나 예술 작품을 볼 때, 말로 표현하기 어려운 경이로움이나 연결감을 느낍니다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'TCI 검사 (상세)' AND s.code = 'ST' AND i.item_number = 1;

UPDATE item i SET "text" = '일상의 작은 순간들(대화, 풍경, 음악 등)이 이상하게도 감사하게 느껴집니다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'TCI 검사 (상세)' AND s.code = 'ST' AND i.item_number = 5;

-- BDI
UPDATE item i SET "text" = '하고 싶던 일에 대한 흥미나 재미가 줄었다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BDI 우울검사' AND s.code = 'D' AND i.item_number = 2;

UPDATE item i SET "text" = '하고 싶던 일에 대한 흥미나 재미가 줄었다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BDI 우울검사 (상세)' AND s.code = 'D' AND i.item_number = 2;
