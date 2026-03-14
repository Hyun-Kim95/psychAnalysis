-- BAI 불안검사 입체적 문장 (TCI 스타일: 상황+반응) - 간단 9문항 + 상세 18문항

-- BAI 불안검사 (간단) 9문항
UPDATE item i SET "text" = '긴장하거나 불안해질 때, 몸이 떨리거나 긴장된 느낌이 든다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사' AND s.code = 'A' AND i.item_number = 1;

UPDATE item i SET "text" = '앞일을 생각하면, 뭔가 잘못될 것 같다는 두려움이 든다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사' AND s.code = 'A' AND i.item_number = 2;

UPDATE item i SET "text" = '불안해지면 가슴이 두근거리거나 숨이 가빠오는 느낌이 든다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사' AND s.code = 'A' AND i.item_number = 3;

UPDATE item i SET "text" = '갑자기 불안하거나 무서운 느낌이 밀려올 때가 있다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사' AND s.code = 'A' AND i.item_number = 4;

UPDATE item i SET "text" = '앞으로 무슨 일이 있을지 불안해서, 일이나 공부에 집중이 잘 안 되는 편이다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사' AND s.code = 'A' AND i.item_number = 5;

UPDATE item i SET "text" = '불안해지면 머리가 어지럽거나 멍한 느낌이 든다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사' AND s.code = 'A' AND i.item_number = 6;

UPDATE item i SET "text" = '긴장하거나 불안해지면 심장이 쿵쾅거리거나 숨이 막히는 느낌이 든다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사' AND s.code = 'A' AND i.item_number = 7;

UPDATE item i SET "text" = '불안한 상황이 오면 손발이 저리거나 땀이 나는 편이다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사' AND s.code = 'A' AND i.item_number = 8;

UPDATE item i SET "text" = '밤에 불안해서 잠들기 어렵거나, 자다가 자주 깨는 일이 있다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사' AND s.code = 'A' AND i.item_number = 9;

-- BAI 불안검사 (상세) 18문항 (1~9 동일, 10~18 추가)
UPDATE item i SET "text" = '긴장하거나 불안해질 때, 몸이 떨리거나 긴장된 느낌이 든다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 1;

UPDATE item i SET "text" = '앞일을 생각하면, 뭔가 잘못될 것 같다는 두려움이 든다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 2;

UPDATE item i SET "text" = '불안해지면 가슴이 두근거리거나 숨이 가빠오는 느낌이 든다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 3;

UPDATE item i SET "text" = '갑자기 불안하거나 무서운 느낌이 밀려올 때가 있다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 4;

UPDATE item i SET "text" = '앞으로 무슨 일이 있을지 불안해서, 일이나 공부에 집중이 잘 안 되는 편이다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 5;

UPDATE item i SET "text" = '불안해지면 머리가 어지럽거나 멍한 느낌이 든다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 6;

UPDATE item i SET "text" = '긴장하거나 불안해지면 심장이 쿵쾅거리거나 숨이 막히는 느낌이 든다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 7;

UPDATE item i SET "text" = '불안한 상황이 오면 손발이 저리거나 땀이 나는 편이다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 8;

UPDATE item i SET "text" = '밤에 불안해서 잠들기 어렵거나, 자다가 자주 깨는 일이 있다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 9;

UPDATE item i SET "text" = '스트레스를 받거나 예민해지면, 불안한 느낌이 더해지는 편이다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 10;

UPDATE item i SET "text" = '불안해지면 숨이 막히거나 헐떡거리는 느낌이 든다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 11;

UPDATE item i SET "text" = '긴장하거나 불안한 순간에는 손이나 몸이 떨린다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 12;

UPDATE item i SET "text" = '특별한 이유 없이도, 어디선가 불안한 느낌이 밀려올 때가 있다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 13;

UPDATE item i SET "text" = '중요한 일을 앞두면 실수할까 봐 불안해지는 편이다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 14;

UPDATE item i SET "text" = '불안해지면 가슴이 조여오는 느낌이 든다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 15;

UPDATE item i SET "text" = '불안이 심해지면 공황처럼 느껴질 때가 있다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 16;

UPDATE item i SET "text" = '긴장하거나 불안해지면 몸이 굳거나 뻣뻣해지는 느낌이 든다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 17;

UPDATE item i SET "text" = '불안이 심할 때는 일상생활이 방해받는 느낌이 든다.'
FROM assessment a, scale s WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 18;
