-- NEO 성격검사 (상세) 50문항 입체적 문장으로 교체 (5척도 × 10문항)

-- N (신경증) 1~10
UPDATE item SET "text" = '먼저 일이 잘못될까 봐 걱정이 먼저 떠오르는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'N' LIMIT 1) AND item_number = 1;
UPDATE item SET "text" = '일이 쌓이거나 마감이 다가오면, 초조함이나 불안이 쉽게 느껴집니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'N' LIMIT 1) AND item_number = 2;
UPDATE item SET "text" = '별일 없는데도 기분이 가라앉고, 하고 싶던 일이 잘 떠오르지 않을 때가 있습니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'N' LIMIT 1) AND item_number = 3;
UPDATE item SET "text" = '사소한 말이나 상황에도 상처받거나 짜증이 나는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'N' LIMIT 1) AND item_number = 4;
UPDATE item SET "text" = '기분이 좋았다가도 금방 무겁거나 불안해지는 등 기복이 있는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'N' LIMIT 1) AND item_number = 5;
UPDATE item SET "text" = '걱정이 많거나 마음이 불안하면, 잠이 오지 않거나 자다가 깨는 일이 있습니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'N' LIMIT 1) AND item_number = 6;
UPDATE item SET "text" = '비판이나 지적을 받으면, 오래 생각하며 상처받는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'N' LIMIT 1) AND item_number = 7;
UPDATE item SET "text" = '걱정이 많아서 마음이 무거운 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'N' LIMIT 1) AND item_number = 8;
UPDATE item SET "text" = '기분이 가라앉았을 때, 다른 일로 전환하기가 어려운 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'N' LIMIT 1) AND item_number = 9;
UPDATE item SET "text" = '내가 불리해지거나 손해를 볼까 봐 걱정이 드는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'N' LIMIT 1) AND item_number = 10;

-- E (외향성) 1~10
UPDATE item SET "text" = '모임이나 행사에 가면, 분위기를 만들거나 말을 걸어 보는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'E' LIMIT 1) AND item_number = 1;
UPDATE item SET "text" = '대화를 시작하거나 주제를 이끌어 가는 것을 부담스럽지 않게 느낍니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'E' LIMIT 1) AND item_number = 2;
UPDATE item SET "text" = '새로운 사람들과의 자리에서 에너지가 올라오는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'E' LIMIT 1) AND item_number = 3;
UPDATE item SET "text" = '친구나 동료와 시간을 보내면, 기분이 좋아지거나 회복된 느낌이 듭니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'E' LIMIT 1) AND item_number = 4;
UPDATE item SET "text" = '여러 사람이 모인 자리에서도 편하게 말하고 움직이는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'E' LIMIT 1) AND item_number = 5;
UPDATE item SET "text" = '모임에서 대화가 끊기면, 제가 주제를 던지거나 분위기를 이어가는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'E' LIMIT 1) AND item_number = 6;
UPDATE item SET "text" = '혼자 있는 시간보다, 누군가와 함께 있는 시간이 더 보람 있게 느껴집니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'E' LIMIT 1) AND item_number = 7;
UPDATE item SET "text" = '낯선 사람과도 먼저 인사하거나 말을 걸어 보는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'E' LIMIT 1) AND item_number = 8;
UPDATE item SET "text" = '재미있거나 신나는 일이 있으면, 적극적으로 참여하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'E' LIMIT 1) AND item_number = 9;
UPDATE item SET "text" = '많은 사람 앞에서 말하거나 발표하는 것이 크게 부담스럽지 않습니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'E' LIMIT 1) AND item_number = 10;

-- O (개방성) 1~10
UPDATE item SET "text" = '처음 보는 아이디어나 방법이 나오면, 호기심이 생겨 더 알아보려 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'O' LIMIT 1) AND item_number = 1;
UPDATE item SET "text" = '책을 읽거나 상상할 때, 머릿속에 장면이나 이야기가 풍부하게 그려지는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'O' LIMIT 1) AND item_number = 2;
UPDATE item SET "text" = '예술 작품이나 자연 풍경을 보며, 느낌이나 생각이 오래 남는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'O' LIMIT 1) AND item_number = 3;
UPDATE item SET "text" = '어떤 주제든, 반대 입장이나 다른 시각도 함께 생각해 보려 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'O' LIMIT 1) AND item_number = 4;
UPDATE item SET "text" = '모르는 분야가 나와도, 배우고 싶다는 마음이 먼저 드는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'O' LIMIT 1) AND item_number = 5;
UPDATE item SET "text" = '삶이나 가치, 의미처럼 추상적인 주제에 대해 생각하는 것을 좋아합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'O' LIMIT 1) AND item_number = 6;
UPDATE item SET "text" = '먹어 보지 않은 음식이나 경험해 보지 않은 문화에 도전해 보는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'O' LIMIT 1) AND item_number = 7;
UPDATE item SET "text" = '그림, 글, 음악처럼 창의적인 활동을 할 때 재미를 느낍니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'O' LIMIT 1) AND item_number = 8;
UPDATE item SET "text" = '일상과 다른 새로운 경험에 거부감 없이 열려 있는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'O' LIMIT 1) AND item_number = 9;
UPDATE item SET "text" = '이상, 가치, 삶의 방향에 대해 생각하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'O' LIMIT 1) AND item_number = 10;

-- A (친화성) 1~10
UPDATE item SET "text" = '처음 만난 사람도, 기본적으로 믿고 대하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'A' LIMIT 1) AND item_number = 1;
UPDATE item SET "text" = '함께 하는 일에서, 내가 조금 더 맞춰도 팀이 잘 되면 괜찮다고 느낍니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'A' LIMIT 1) AND item_number = 2;
UPDATE item SET "text" = '상대방이 왜 그렇게 말했는지, 입장을 떠올리며 이해하려 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'A' LIMIT 1) AND item_number = 3;
UPDATE item SET "text" = '의견이 엇갈려도, 싸우기보다 맞춰 가는 쪽을 선호합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'A' LIMIT 1) AND item_number = 4;
UPDATE item SET "text" = '상대가 힘들어 보이면, 도움이 될 말이나 행동을 찾아보는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'A' LIMIT 1) AND item_number = 5;
UPDATE item SET "text" = '다른 사람의 단점보다 장점을 먼저 보려고 하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'A' LIMIT 1) AND item_number = 6;
UPDATE item SET "text" = '주변 사람이 잘 되거나 성공하면, 같이 기뻐해 주는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'A' LIMIT 1) AND item_number = 7;
UPDATE item SET "text" = '속이거나 편애하지 않고, 정직하고 공정하게 행동하려 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'A' LIMIT 1) AND item_number = 8;
UPDATE item SET "text" = '이기고 지는 것보다, 함께 좋은 결과를 내는 쪽을 선호합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'A' LIMIT 1) AND item_number = 9;
UPDATE item SET "text" = '상대의 표정이나 말투에서 기분이나 감정을 읽으려 하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'A' LIMIT 1) AND item_number = 10;

-- C (성실성) 1~10
UPDATE item SET "text" = '해야 할 일이 있으면, 미루지 않고 가능한 한 빨리 손대려고 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 1;
UPDATE item SET "text" = '큰 일은 단계를 나눠서 계획하고, 그에 맞춰 실행하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 2;
UPDATE item SET "text" = '맡은 일은 끝까지 하고, 세부 사항도 놓치지 않으려 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 3;
UPDATE item SET "text" = '목표를 정해두면, 달성할 때까지 꾸준히 노력하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 4;
UPDATE item SET "text" = '약속이나 규칙은, 가능한 한 지키려고 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 5;
UPDATE item SET "text" = '시작한 일은 마무리할 때까지 끝까지 하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 6;
UPDATE item SET "text" = '할 일과 시간을 정해두고, 그에 맞춰 움직이는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 7;
UPDATE item SET "text" = '세부 사항이나 디테일을 놓치지 않으려고 확인하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 8;
UPDATE item SET "text" = '목표 달성을 위해 필요한 단계를 생각하고 실행하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 9;
UPDATE item SET "text" = '한 번 한 약속은 지키려고 노력하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'NEO 성격검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 10;
