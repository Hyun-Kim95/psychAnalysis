-- BDI 우울검사(간단) 9문항 문장 개선: 구체적·직설적으로

UPDATE item SET "text" = '기분이 가라앉고 마음이 무겁다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 1;

UPDATE item SET "text" = '재미있던 일, 하고 싶던 일에 흥미가 떨어진다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 2;

UPDATE item SET "text" = '잠들기 힘들거나 자다가 자주 깬다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 3;

UPDATE item SET "text" = '할 일이 없어도 피곤하고 기운이 없다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 4;

UPDATE item SET "text" = '맛이 없어 먹기가 줄었거나, 스트레스로 많이 먹게 된다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 5;

UPDATE item SET "text" = '내가 나쁜 사람 같고, 잘못된 사람 같다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 6;

UPDATE item SET "text" = '일에, 공부에, 일상에 집중이 잘 안 된다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 7;

UPDATE item SET "text" = '말하고 움직이는 게 느려진 느낌이 들거나, 반대로 안절부절해 가만히 있기 어렵다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 8;

UPDATE item SET "text" = '차라리 없었으면 좋겠다는 생각이 든다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 9;
