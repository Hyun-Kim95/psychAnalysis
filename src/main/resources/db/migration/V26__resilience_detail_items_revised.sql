-- 회복탄력성 검사 (상세) 20문항 입체적 문장으로 수정 (TCI 스타일: 상황+반응)

-- 1~10 (간단과 동일 톤, 입체적 문장)
UPDATE item SET "text" = '힘든 일이 생기면, 원인을 돌아보고 방법을 바꿔가며 이겨내려 하는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 1;

UPDATE item SET "text" = '실패한 뒤에도 "한 번 더 해보자"고 생각하며 다시 시도하는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 2;

UPDATE item SET "text" = '스트레스가 쌓여도, 잠시 쉬거나 할 일을 나눠가며 적응해 나가는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 3;

UPDATE item SET "text" = '힘든 시기가 와도, 결국 잘 되리라고 믿으려 노력하는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 4;

UPDATE item SET "text" = '문제가 생기면 원인을 생각하고, 할 수 있는 것부터 해결 방법을 찾으려 한다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 5;

UPDATE item SET "text" = '계획이 바뀌거나 앞일이 불확실해도, 당황하기보다 받아들이며 대처하는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 6;

UPDATE item SET "text" = '힘들 때 의지할 수 있는 사람이 있다고 느낀다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 7;

UPDATE item SET "text" = '어려운 시기가 와도 "결국 잘 되겠지"라고 믿는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 8;

UPDATE item SET "text" = '힘든 경험을 통해서도 뭔가 배우고 성장할 수 있다고 생각한다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 9;

UPDATE item SET "text" = '일상에서 작은 일이 잘 풀리거나 도움이 되면, "잘했어", "다행이야"라고 느끼는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 10;

-- 11~20 (추가 문항 입체적 문장)
UPDATE item SET "text" = '좌절했을 때도, 며칠 지나지 않아 "다시 해보자"는 마음이 드는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 11;

UPDATE item SET "text" = '선택이 어렵고 불안해도, 정보를 모은 뒤 결국 결정을 내리는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 12;

UPDATE item SET "text" = '일이 꼬이거나 압박이 있어도, 감정이 올라와도 한번 숨 고르고 대처하려 한다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 13;

UPDATE item SET "text" = '목표가 흔들리거나 무너져도, "이번엔 이렇게 해보자"며 다시 세워 나가는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 14;

UPDATE item SET "text" = '힘들 때 주변에 도움을 요청할 수 있는 사람이 있다고 느낀다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 15;

UPDATE item SET "text" = '예전에 비슷한 어려움을 겪었는데, 결국 이겨낸 경험이 있다고 느낀다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 16;

UPDATE item SET "text" = '새로운 환경이나 역할이 주어져도, 얼마 지나지 않아 적응하는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 17;

UPDATE item SET "text" = '"나는 이걸 해낼 수 있어"라고 느낄 때가 많다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 18;

UPDATE item SET "text" = '힘든 일이 있어도, "이걸 통해 뭔가 얻는 게 있겠지"라고 생각하려 하는 편이다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 19;

UPDATE item SET "text" = '삶의 여러 부분을 돌아보면, 전반적으로 만족스럽다고 느낀다.'
WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND scale_id = (SELECT id FROM scale WHERE assessment_id = (SELECT id FROM assessment WHERE name = '회복탄력성 검사 (상세)' LIMIT 1) AND code = 'R' LIMIT 1) AND item_number = 20;
