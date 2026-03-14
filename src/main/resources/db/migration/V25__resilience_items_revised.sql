-- 회복탄력성 검사(간단) 10문항 입체적 문장으로 수정 (TCI 스타일: 상황+반응)

UPDATE item SET "text" = '힘든 일이 생기면, 원인을 돌아보고 방법을 바꿔가며 이겨내려 하는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 1;

UPDATE item SET "text" = '실패한 뒤에도 "한 번 더 해보자"고 생각하며 다시 시도하는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 2;

UPDATE item SET "text" = '스트레스가 쌓여도, 잠시 쉬거나 할 일을 나눠가며 적응해 나가는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 3;

UPDATE item SET "text" = '힘든 시기가 와도, 결국 잘 되리라고 믿으려 노력하는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 4;

UPDATE item SET "text" = '문제가 생기면 원인을 생각하고, 할 수 있는 것부터 해결 방법을 찾으려 한다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 5;

UPDATE item SET "text" = '계획이 바뀌거나 앞일이 불확실해도, 당황하기보다 받아들이며 대처하는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 6;

UPDATE item SET "text" = '힘들 때 의지할 수 있는 사람이 있다고 느낀다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 7;

UPDATE item SET "text" = '어려운 시기가 와도 "결국 잘 되겠지"라고 믿는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 8;

UPDATE item SET "text" = '힘든 경험을 통해서도 뭔가 배우고 성장할 수 있다고 생각한다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 9;

UPDATE item SET "text" = '일상에서 작은 일이 잘 풀리거나 도움이 되면, "잘했어", "다행이야"라고 느끼는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 10;
