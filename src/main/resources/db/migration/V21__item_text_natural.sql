-- 문항 문장 어색한 표현 수정 (하고 싶은→하고 싶던, 때가 있습니다→편입니다/느낍니다 등)

-- NEO 성격검사 간단: N-3 (하고 싶은→하고 싶던), E-4 (회복되는→회복된)
UPDATE item SET "text" = '별일 없는데도 기분이 가라앉고, 하고 싶던 일이 잘 떠오르지 않을 때가 있습니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'N' LIMIT 1) AND item_number = 3;

UPDATE item SET "text" = '친구나 동료와 시간을 보내면, 기분이 좋아지거나 회복된 느낌이 듭니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'E' LIMIT 1) AND item_number = 4;

-- NEO 성격검사 상세: N-3, N-8, E-4, O-10
UPDATE item SET "text" = '별일 없는데도 기분이 가라앉고, 하고 싶던 일이 잘 떠오르지 않을 때가 있습니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'N' LIMIT 1) AND item_number = 3;

UPDATE item SET "text" = '걱정이 많아서 마음이 무거운 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'N' LIMIT 1) AND item_number = 8;

UPDATE item SET "text" = '친구나 동료와 시간을 보내면, 기분이 좋아지거나 회복된 느낌이 듭니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'E' LIMIT 1) AND item_number = 4;

UPDATE item SET "text" = '이상, 가치, 삶의 방향에 대해 생각하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'O' LIMIT 1) AND item_number = 10;

-- TCI 검사 간단: RD-4, ST-1, ST-5
UPDATE item SET "text" = '중요한 결정을 할 때, 내가 원하는 것보다 주변 사람이 어떻게 생각할까를 먼저 떠올리는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' LIMIT 1) AND code = 'RD' LIMIT 1) AND item_number = 4;

UPDATE item SET "text" = '자연이나 예술 작품을 볼 때, 말로 표현하기 어려운 경이로움이나 연결감을 느낍니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' LIMIT 1) AND code = 'ST' LIMIT 1) AND item_number = 1;

UPDATE item SET "text" = '일상의 작은 순간들(대화, 풍경, 음악 등)이 이상하게도 감사하게 느껴집니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' LIMIT 1) AND code = 'ST' LIMIT 1) AND item_number = 5;

-- TCI 검사 상세: RD-4, RD-7, ST-1, ST-5
UPDATE item SET "text" = '중요한 결정을 할 때, 내가 원하는 것보다 주변 사람이 어떻게 생각할까를 먼저 떠올리는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'RD' LIMIT 1) AND item_number = 4;

UPDATE item SET "text" = '상대방이 제 말이나 행동에 어떻게 반응했는지, 나중에도 자꾸 생각하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'RD' LIMIT 1) AND item_number = 7;

UPDATE item SET "text" = '자연이나 예술 작품을 볼 때, 말로 표현하기 어려운 경이로움이나 연결감을 느낍니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'ST' LIMIT 1) AND item_number = 1;

UPDATE item SET "text" = '일상의 작은 순간들(대화, 풍경, 음악 등)이 이상하게도 감사하게 느껴집니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'ST' LIMIT 1) AND item_number = 5;

-- BDI: 2번 문항 '하고 싶은' → '하고 싶던' (V10 초기 데이터 또는 문장 통일)
UPDATE item SET "text" = '하고 싶던 일에 대한 흥미나 재미가 줄었다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 2;

UPDATE item SET "text" = '하고 싶던 일에 대한 흥미나 재미가 줄었다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 2;
