-- TCI 검사 (상세) 70문항을 입체적 문장으로 교체 (7척도 × 10문항)

-- NS (자극추구) 1~10
UPDATE item SET "text" = '새로운 사람들과 어울리는 모임에 가면, 어색하더라도 금방 분위기에 섞이는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'NS' LIMIT 1) AND item_number = 1;
UPDATE item SET "text" = '여행을 갈 때, 계획되지 않은 즉흥적인 코스를 넣어보는 걸 즐깁니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'NS' LIMIT 1) AND item_number = 2;
UPDATE item SET "text" = '반복되는 일상보다, 어느 정도 예측 불가능한 날들이 더 활력을 준다고 느낍니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'NS' LIMIT 1) AND item_number = 3;
UPDATE item SET "text" = '다른 사람들이 망설이는 새로운 아이디어를, 일단 해보자고 먼저 제안하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'NS' LIMIT 1) AND item_number = 4;
UPDATE item SET "text" = '요즘 삶이 너무 단조롭다고 느끼면, 일부러라도 새로운 경험을 찾으려고 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'NS' LIMIT 1) AND item_number = 5;
UPDATE item SET "text" = '처음 해보는 일이라도, 설명만 듣고 곧바로 시도해 보는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'NS' LIMIT 1) AND item_number = 6;
UPDATE item SET "text" = '같은 장소만 다니기보다, 가보지 않은 곳이나 새로운 메뉴를 골라 보는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'NS' LIMIT 1) AND item_number = 7;
UPDATE item SET "text" = '모르는 분야에 대한 이야기가 나오면, 궁금해서 더 물어보거나 찾아보는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'NS' LIMIT 1) AND item_number = 8;
UPDATE item SET "text" = '계획이 갑자기 바뀌어도, 불편하기보다 ''이렇게 가는구나'' 하고 받아들이는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'NS' LIMIT 1) AND item_number = 9;
UPDATE item SET "text" = '새로운 팀이나 환경에 들어가면, 얼마 지나지 않아 제자리를 찾는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'NS' LIMIT 1) AND item_number = 10;

-- HA (위험회피) 1~10
UPDATE item SET "text" = '처음 가보는 장소나 낯선 사람들과의 자리에서는, 잘못될 수 있는 상황을 먼저 떠올리는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'HA' LIMIT 1) AND item_number = 1;
UPDATE item SET "text" = '중요한 결정을 앞두고는, ''혹시 실패하면 어쩌지''라는 생각이 여러 번 떠오릅니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'HA' LIMIT 1) AND item_number = 2;
UPDATE item SET "text" = '다른 사람들이 별일 아니라고 해도, 저는 걱정이 쉽게 가라앉지 않을 때가 많습니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'HA' LIMIT 1) AND item_number = 3;
UPDATE item SET "text" = '계획에 없던 일이 생기면, 호기심보다 불안이 먼저 느껴집니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'HA' LIMIT 1) AND item_number = 4;
UPDATE item SET "text" = '새로운 일에 도전하기 전, 충분히 안전하다고 느끼지 못하면 쉽게 시작하지 못합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'HA' LIMIT 1) AND item_number = 5;
UPDATE item SET "text" = '선택지가 여러 개일 때, 틀린 결정을 내릴까 봐 결단을 미루는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'HA' LIMIT 1) AND item_number = 6;
UPDATE item SET "text" = '새로운 업무나 역할을 맡을 때, 잘해낼 수 있을지 먼저 걱정이 됩니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'HA' LIMIT 1) AND item_number = 7;
UPDATE item SET "text" = '의견을 말하거나 발표할 때, 비판받거나 무시당할까 봐 신경 쓰입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'HA' LIMIT 1) AND item_number = 8;
UPDATE item SET "text" = '몸에 이상이 있으면, 큰 문제는 아닐까 하고 자주 생각하게 됩니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'HA' LIMIT 1) AND item_number = 9;
UPDATE item SET "text" = '실수나 사고가 나지 않도록, 할 수 있는 만큼 꼼꼼히 확인하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'HA' LIMIT 1) AND item_number = 10;

-- RD (보상 의존) 1~10
UPDATE item SET "text" = '누군가가 제 이야기를 진심으로 들어줄 때, 그 순간이 오래 기억에 남는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'RD' LIMIT 1) AND item_number = 1;
UPDATE item SET "text" = '가까운 사람이 저에게 서운했다는 표현을 하면, 한동안 계속 신경 쓰이는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'RD' LIMIT 1) AND item_number = 2;
UPDATE item SET "text" = '내가 한 행동에 대해 주변에서 칭찬이나 인정을 받으면, 더 힘이 나서 계속하게 됩니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'RD' LIMIT 1) AND item_number = 3;
UPDATE item SET "text" = '중요한 결정을 할 때, 내가 원하는 것보다 주변 사람이 어떻게 생각할까를 먼저 떠올리는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'RD' LIMIT 1) AND item_number = 4;
UPDATE item SET "text" = '친한 사람과의 관계가 서먹해졌다고 느끼면, 대화를 시도하며 관계를 회복하려 노력합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'RD' LIMIT 1) AND item_number = 5;
UPDATE item SET "text" = '힘든 일이 있을 때, 믿는 사람과 이야기하면 마음이 조금 가벼워지는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'RD' LIMIT 1) AND item_number = 6;
UPDATE item SET "text" = '상대방이 제 말이나 행동에 어떻게 반응했는지, 나중에도 자꾸 생각하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'RD' LIMIT 1) AND item_number = 7;
UPDATE item SET "text" = '속한 팀이나 모임이 있으면, ''여기 소속이다''라는 느낌이 마음을 든든하게 해줍니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'RD' LIMIT 1) AND item_number = 8;
UPDATE item SET "text" = '도움이 필요할 때, 거절당할까 봐 망설이기보다 부담 없이 요청하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'RD' LIMIT 1) AND item_number = 9;
UPDATE item SET "text" = '다른 사람이 나를 어떻게 평가하는지, 생각보다 신경 쓰이는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'RD' LIMIT 1) AND item_number = 10;

-- P (인내력) 1~10
UPDATE item SET "text" = '흥미가 조금 떨어져도, 한 번 시작한 일은 웬만하면 끝까지 마무리하려 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'P' LIMIT 1) AND item_number = 1;
UPDATE item SET "text" = '성과가 바로 보이지 않는 일이라도, 의미가 있다고 느끼면 꾸준히 이어가는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'P' LIMIT 1) AND item_number = 2;
UPDATE item SET "text" = '피곤하고 하기 싫은 날에도, 해야 할 일이라면 최소한의 분량은 지키려고 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'P' LIMIT 1) AND item_number = 3;
UPDATE item SET "text" = '작은 실패가 여러 번 이어져도, ''방법을 바꿔보자''고 생각하며 다시 시도하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'P' LIMIT 1) AND item_number = 4;
UPDATE item SET "text" = '주변에서 그만해도 된다고 말해도, 제가 필요하다고 느끼면 계속 버티며 해내려 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'P' LIMIT 1) AND item_number = 5;
UPDATE item SET "text" = '목표를 정해두면, 달성할 때까지 단계를 나눠서라도 꾸준히 다가가는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'P' LIMIT 1) AND item_number = 6;
UPDATE item SET "text" = '어려운 과제가 주어지면, 한 번에 안 되더라도 나눠서 끝까지 해내려고 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'P' LIMIT 1) AND item_number = 7;
UPDATE item SET "text" = '실패한 뒤에는 원인을 돌아보고, 같은 실수를 줄이려고 다시 도전하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'P' LIMIT 1) AND item_number = 8;
UPDATE item SET "text" = '당장의 만족보다, 나중에 더 좋은 결과를 위해 기다리고 참는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'P' LIMIT 1) AND item_number = 9;
UPDATE item SET "text" = '해야 할 일이 있으면, 미루지 않고 가능한 한 빨리 손대려고 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'P' LIMIT 1) AND item_number = 10;

-- SD (자율성) 1~10
UPDATE item SET "text" = '중요한 선택을 할 때, 주변 의견을 듣더라도 최종 결정은 내가 책임져야 한다고 느낍니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'SD' LIMIT 1) AND item_number = 1;
UPDATE item SET "text" = '힘든 일이 있어도, 결국 내 삶은 내가 선택한 방향이라고 생각하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'SD' LIMIT 1) AND item_number = 2;
UPDATE item SET "text" = '남들이 기대하는 모습과 다르더라도, 나에게 맞는 길이라면 용기 내어 선택하려 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'SD' LIMIT 1) AND item_number = 3;
UPDATE item SET "text" = '스스로 정한 기준이나 가치관을, 상황에 따라 쉽게 바꾸지 않으려고 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'SD' LIMIT 1) AND item_number = 4;
UPDATE item SET "text" = '힘든 상황에서도 ''내가 할 수 있는 부분은 무엇인지'' 먼저 살펴보려는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'SD' LIMIT 1) AND item_number = 5;
UPDATE item SET "text" = '다른 사람이 뭐라고 하든, 중요한 일은 제가 납득할 수 있는 기준으로 판단하려고 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'SD' LIMIT 1) AND item_number = 6;
UPDATE item SET "text" = '하고 싶은 일이 생기면, 주변 반대가 있어도 스스로 계획을 세워 조금씩 실천하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'SD' LIMIT 1) AND item_number = 7;
UPDATE item SET "text" = '잘못을 했을 때, 변명보다는 ''어디를 고치면 될까''를 먼저 생각하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'SD' LIMIT 1) AND item_number = 8;
UPDATE item SET "text" = '할 일과 시간을 스스로 정해두고, 그에 맞춰 조절하며 지내는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'SD' LIMIT 1) AND item_number = 9;
UPDATE item SET "text" = '내가 무엇을 좋아하고, 무엇에 약한지에 대해 생각해 본 적이 있습니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'SD' LIMIT 1) AND item_number = 10;

-- C (협동성) 1~10
UPDATE item SET "text" = '다른 사람이 힘들어 보이면, 잘 모르는 사이라도 도와줄 수 있는 부분이 있는지 살펴보는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 1;
UPDATE item SET "text" = '팀으로 일할 때, 내가 조금 더 수고하더라도 전체가 잘 돌아가면 괜찮다고 느낍니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 2;
UPDATE item SET "text" = '의견이 다르더라도, 상대를 이기기보다 서로 받아들일 수 있는 지점을 찾으려 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 3;
UPDATE item SET "text" = '약속을 지키지 못했을 때, 상대가 어떤 기분일지 떠올리며 미안함을 크게 느끼는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 4;
UPDATE item SET "text" = '규칙이나 공정성이 무너지는 상황을 보면, 내 일이 아니어도 불편함을 느낍니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 5;
UPDATE item SET "text" = '함께 하는 일에서, 내가 맡은 역할을 끝까지 해내려고 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 6;
UPDATE item SET "text" = '상대방이 말할 때, 말을 끊기보다 끝까지 듣고 나서 의견을 말하는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 7;
UPDATE item SET "text" = '내가 이기는 것보다, 함께 좋은 결과를 내는 쪽을 더 중요하게 생각합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 8;
UPDATE item SET "text" = '부당한 일이 눈에 띄면, 참고 지나치기보다는 어떻게 할지 생각하게 됩니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 9;
UPDATE item SET "text" = '힘에 겨워하는 사람을 보면, 제가 할 수 있는 범위에서 배려하려고 합니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'C' LIMIT 1) AND item_number = 10;

-- ST (자기초월) 1~10
UPDATE item SET "text" = '자연이나 예술 작품을 볼 때, 말로 표현하기 어려운 경이로움이나 연결감을 느낍니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'ST' LIMIT 1) AND item_number = 1;
UPDATE item SET "text" = '힘든 시기를 지나면서, 그 경험이 내 삶의 의미나 방향을 다시 생각하게 만든 적이 있습니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'ST' LIMIT 1) AND item_number = 2;
UPDATE item SET "text" = '혼자 있을 때, ''나는 어떤 삶을 살고 싶은 사람인가'' 같은 질문을 스스로에게 던져보는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'ST' LIMIT 1) AND item_number = 3;
UPDATE item SET "text" = '나보다 큰 무언가(사회, 자연, 가치 등)를 위해 기여하고 싶다는 마음이 가끔 강하게 떠오릅니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'ST' LIMIT 1) AND item_number = 4;
UPDATE item SET "text" = '일상의 작은 순간들(대화, 풍경, 음악 등)이 이상하게도 감사하게 느껴집니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'ST' LIMIT 1) AND item_number = 5;
UPDATE item SET "text" = '예술 작품이나 자연 풍경을 보다가, 가슴이 뭉클해지거나 감동한 적이 있습니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'ST' LIMIT 1) AND item_number = 6;
UPDATE item SET "text" = '내가 왜 살고, 무엇을 위해 움직이는지를 가끔 생각하는 시간을 가집니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'ST' LIMIT 1) AND item_number = 7;
UPDATE item SET "text" = '살아가면서 ''이건 지키고 싶다''는 나만의 가치관이나 기준이 생겼습니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'ST' LIMIT 1) AND item_number = 8;
UPDATE item SET "text" = '사소한 일상 속에서도 의미나 배움을 찾는 편입니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'ST' LIMIT 1) AND item_number = 9;
UPDATE item SET "text" = '물질적 보상보다, 하고 싶은 일을 하거나 관계에서 오는 만족을 더 중요하게 느낍니다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사 (상세)' LIMIT 1) AND code = 'ST' LIMIT 1) AND item_number = 10;
