-- BAI 2번: 한국어 「흥분」은 긍정적 들뜸으로 오해하기 쉬움. 영어 원문 "Feeling hot"(열감·화끈거림) 의미로 정렬.
UPDATE item SET "text" = '몸이나 얼굴이 화끈거리거나, 뜨거워지는 느낌이 든다.'
WHERE assessment_id IN (SELECT id FROM assessment WHERE name IN ('BAI 불안검사', 'BAI 불안검사 (상세)'))
  AND item_number = 2;
