-- 선택지 문구 '거의 매일' → '거의 항상' 변경 (BDI, BAI 등)
UPDATE choice SET label = '거의 항상' WHERE label = '거의 매일';
