-- BDI 우울검사 입체적 문장 (TCI 스타일: 상황+반응) - 간단 9문항 + 상세 18문항

-- BDI 우울검사 (간단) 9문항
UPDATE item SET "text" = '아무 일 없는데도 기분이 가라앉고, 마음이 무겁게 느껴질 때가 있다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 1;

UPDATE item SET "text" = '예전에 재미있던 일이나 하고 싶던 일을 떠올려도, 흥미가 잘 안 붙는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 2;

UPDATE item SET "text" = '밤에 누워도 잠들기 힘들거나, 자다가 자주 깨는 일이 있다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 3;

UPDATE item SET "text" = '쉬는 날이나 할 일이 없어도, 피곤하고 기운이 없게 느껴진다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 4;

UPDATE item SET "text" = '밥맛이 없어 먹기가 줄었거나, 스트레스를 받으면 평소보다 많이 먹게 되는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 5;

UPDATE item SET "text" = '뭔가 잘못했을 때뿐 아니라, 그냥 있을 때도 내가 나쁜 사람 같고 잘못된 사람 같다는 생각이 든다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 6;

UPDATE item SET "text" = '일이나 공부, 일상을 할 때도 머리가 어리둥절해서 집중이 잘 안 되는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 7;

UPDATE item SET "text" = '말하고 움직이는 게 느려진 느낌이 들거나, 반대로 안절부절해서 가만히 있기 어려울 때가 있다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 8;

UPDATE item SET "text" = '힘들거나 지칠 때, 차라리 내가 없었으면 좋겠다는 생각이 든다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 9;

-- BDI 우울검사 (상세) 18문항 (1~9 동일, 10~18 추가)
UPDATE item SET "text" = '아무 일 없는데도 기분이 가라앉고, 마음이 무겁게 느껴질 때가 있다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 1;

UPDATE item SET "text" = '예전에 재미있던 일이나 하고 싶던 일을 떠올려도, 흥미가 잘 안 붙는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 2;

UPDATE item SET "text" = '밤에 누워도 잠들기 힘들거나, 자다가 자주 깨는 일이 있다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 3;

UPDATE item SET "text" = '쉬는 날이나 할 일이 없어도, 피곤하고 기운이 없게 느껴진다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 4;

UPDATE item SET "text" = '밥맛이 없어 먹기가 줄었거나, 스트레스를 받으면 평소보다 많이 먹게 되는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 5;

UPDATE item SET "text" = '뭔가 잘못했을 때뿐 아니라, 그냥 있을 때도 내가 나쁜 사람 같고 잘못된 사람 같다는 생각이 든다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 6;

UPDATE item SET "text" = '일이나 공부, 일상을 할 때도 머리가 어리둥절해서 집중이 잘 안 되는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 7;

UPDATE item SET "text" = '말하고 움직이는 게 느려진 느낌이 들거나, 반대로 안절부절해서 가만히 있기 어려울 때가 있다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 8;

UPDATE item SET "text" = '힘들거나 지칠 때, 차라리 내가 없었으면 좋겠다는 생각이 든다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 9;

UPDATE item SET "text" = '앞날을 생각해 봐도 나아질 것 같지 않고, 희망이 잘 안 보이는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 10;

UPDATE item SET "text" = '뭘 해야 할지 막막하고, 시작할 의욕이 잘 안 날 때가 많다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 11;

UPDATE item SET "text" = '예전엔 쉽게 하던 작은 일도, 지금은 부담되고 하기 싫게 느껴진다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 12;

UPDATE item SET "text" = '잠을 못 자서 밤을 새우거나, 많이 자도 피곤하게 느껴질 때가 있다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 13;

UPDATE item SET "text" = '일이 잘못되면 내 탓이라고 생각하거나, 과거를 후회해서 괴로울 때가 있다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 14;

UPDATE item SET "text" = '선택이나 결정을 내려야 할 때, 무엇을 골라야 할지 어렵게 느껴진다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 15;

UPDATE item SET "text" = '내가 가치 없다고 느끼거나, 남에게 짐이 되는 것 같을 때가 있다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 16;

UPDATE item SET "text" = '할 일이 있어도 에너지가 없고, 뭘 해도 무기력하게 느껴진다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 17;

UPDATE item SET "text" = '밥 먹기, 씻기, 밖에 나가기 같은 일상을 할 때도 힘들게 느껴진다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BDI 우울검사 (상세)' LIMIT 1) AND code = 'D' LIMIT 1) AND item_number = 18;
