-- NEO 성격검사(간단) 25문항 입체적 문장으로 교체 (5척도 × 5문항)

-- N (신경증) 1~5
UPDATE item SET "text" = '먼저 일이 잘못될까 봐 걱정이 먼저 떠오르는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'N' LIMIT 1) AND item_number = 1;
UPDATE item SET "text" = '일이 쌓이거나 마감이 다가오면, 초조함이나 불안이 쉽게 느껴집니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'N' LIMIT 1) AND item_number = 2;
UPDATE item SET "text" = '별일 없는데도 기분이 가라앉고, 하고 싶던 일이 잘 떠오르지 않을 때가 있습니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'N' LIMIT 1) AND item_number = 3;
UPDATE item SET "text" = '사소한 말이나 상황에도 상처받거나 짜증이 나는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'N' LIMIT 1) AND item_number = 4;
UPDATE item SET "text" = '기분이 좋았다가도 금방 무겁거나 불안해지는 등 기복이 있는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'N' LIMIT 1) AND item_number = 5;

-- E (외향성) 1~5
UPDATE item SET "text" = '모임이나 행사에 가면, 분위기를 만들거나 말을 걸어 보는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'E' LIMIT 1) AND item_number = 1;
UPDATE item SET "text" = '대화를 시작하거나 주제를 이끌어 가는 것을 부담스럽지 않게 느낍니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'E' LIMIT 1) AND item_number = 2;
UPDATE item SET "text" = '새로운 사람들과의 자리에서 에너지가 올라오는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'E' LIMIT 1) AND item_number = 3;
UPDATE item SET "text" = '친구나 동료와 시간을 보내면, 기분이 좋아지거나 회복된 느낌이 듭니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'E' LIMIT 1) AND item_number = 4;
UPDATE item SET "text" = '여러 사람이 모인 자리에서도 편하게 말하고 움직이는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'E' LIMIT 1) AND item_number = 5;

-- O (개방성) 1~5
UPDATE item SET "text" = '처음 보는 아이디어나 방법이 나오면, 호기심이 생겨 더 알아보려 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'O' LIMIT 1) AND item_number = 1;
UPDATE item SET "text" = '책을 읽거나 상상할 때, 머릿속에 장면이나 이야기가 풍부하게 그려지는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'O' LIMIT 1) AND item_number = 2;
UPDATE item SET "text" = '예술 작품이나 자연 풍경을 보며, 느낌이나 생각이 오래 남는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'O' LIMIT 1) AND item_number = 3;
UPDATE item SET "text" = '어떤 주제든, 반대 입장이나 다른 시각도 함께 생각해 보려 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'O' LIMIT 1) AND item_number = 4;
UPDATE item SET "text" = '모르는 분야가 나와도, 배우고 싶다는 마음이 먼저 드는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'O' LIMIT 1) AND item_number = 5;

-- A (친화성) 1~5
UPDATE item SET "text" = '처음 만난 사람도, 기본적으로 믿고 대하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'A' LIMIT 1) AND item_number = 1;
UPDATE item SET "text" = '함께 하는 일에서, 내가 조금 더 맞춰도 팀이 잘 되면 괜찮다고 느낍니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'A' LIMIT 1) AND item_number = 2;
UPDATE item SET "text" = '상대방이 왜 그렇게 말했는지, 입장을 떠올리며 이해하려 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'A' LIMIT 1) AND item_number = 3;
UPDATE item SET "text" = '의견이 엇갈려도, 싸우기보다 맞춰 가는 쪽을 선호합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'A' LIMIT 1) AND item_number = 4;
UPDATE item SET "text" = '상대가 힘들어 보이면, 도움이 될 말이나 행동을 찾아보는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'A' LIMIT 1) AND item_number = 5;

-- C (성실성) 1~5
UPDATE item SET "text" = '해야 할 일이 있으면, 미루지 않고 가능한 한 빨리 손대려고 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 1;
UPDATE item SET "text" = '큰 일은 단계를 나눠서 계획하고, 그에 맞춰 실행하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 2;
UPDATE item SET "text" = '맡은 일은 끝까지 하고, 세부 사항도 놓치지 않으려 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 3;
UPDATE item SET "text" = '목표를 정해두면, 달성할 때까지 꾸준히 노력하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 4;
UPDATE item SET "text" = '약속이나 규칙은, 가능한 한 지키려고 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 5;
