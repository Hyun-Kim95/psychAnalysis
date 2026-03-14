-- BAI 불안검사 (상세) 18문항 문장 개선: 직설적·자연스럽게

UPDATE item i SET "text" = '몸이 떨리거나 긴장된 느낌이 든다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 1;

UPDATE item i SET "text" = '뭔가 잘못될 것 같다는 두려움이 든다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 2;

UPDATE item i SET "text" = '가슴이 두근거리거나 숨이 가쁘다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 3;

UPDATE item i SET "text" = '갑자기 불안하거나 무서운 느낌이 든다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 4;

UPDATE item i SET "text" = '앞으로 무슨 일이 있을지 불안해 집중이 잘 안 된다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 5;

UPDATE item i SET "text" = '머리가 어지럽거나 멍한 느낌이 든다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 6;

UPDATE item i SET "text" = '심장이 쿵쾅거리거나 숨이 막히는 느낌이 든다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 7;

UPDATE item i SET "text" = '손발이 저리거나 땀이 난다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 8;

UPDATE item i SET "text" = '불안해서 잠들기 어렵거나 자다가 자주 깬다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 9;

UPDATE item i SET "text" = '신경이 예민해져서 불안하다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 10;

UPDATE item i SET "text" = '숨이 막히거나 헐떡거린다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 11;

UPDATE item i SET "text" = '손이나 몸이 떨린다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 12;

UPDATE item i SET "text" = '어디선가 불안한 느낌이 든다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 13;

UPDATE item i SET "text" = '실수할까 봐 불안하다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 14;

UPDATE item i SET "text" = '가슴이 조여오는 느낌이 든다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 15;

UPDATE item i SET "text" = '공황처럼 느껴진다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 16;

UPDATE item i SET "text" = '몸이 굳거나 뻣뻣해진다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 17;

UPDATE item i SET "text" = '불안 때문에 일상생활이 방해받는다.'
FROM assessment a, scale s
WHERE i.assessment_id = a.id AND i.scale_id = s.id AND s.assessment_id = a.id
  AND a.name = 'BAI 불안검사 (상세)' AND s.code = 'A' AND i.item_number = 18;
