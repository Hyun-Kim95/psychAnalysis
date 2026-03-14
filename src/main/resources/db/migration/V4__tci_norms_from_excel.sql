-- TCI 규준을 엑셀(9932A73F5D23FA661B.xlsx) 하위척도 기준으로 반영
-- 대척도별 평균·표준편차 = 해당 하위척도 평균·표준편차의 산술평균

UPDATE norm n
SET mean = 6.925, sd = 3.225
FROM scale s
WHERE n.scale_id = s.id AND n.assessment_id = s.assessment_id
  AND s.code = 'NS' AND s.assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' LIMIT 1);

UPDATE norm n
SET mean = 8.775, sd = 3.525
FROM scale s
WHERE n.scale_id = s.id AND n.assessment_id = s.assessment_id
  AND s.code = 'HA' AND s.assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' LIMIT 1);

UPDATE norm n
SET mean = 10.65, sd = 2.95
FROM scale s
WHERE n.scale_id = s.id AND n.assessment_id = s.assessment_id
  AND s.code = 'RD' AND s.assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' LIMIT 1);

UPDATE norm n
SET mean = 10.925, sd = 3.3
FROM scale s
WHERE n.scale_id = s.id AND n.assessment_id = s.assessment_id
  AND s.code = 'P'  AND s.assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' LIMIT 1);

UPDATE norm n
SET mean = 9.56, sd = 2.52
FROM scale s
WHERE n.scale_id = s.id AND n.assessment_id = s.assessment_id
  AND s.code = 'SD' AND s.assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' LIMIT 1);

UPDATE norm n
SET mean = 11.22, sd = 2.58
FROM scale s
WHERE n.scale_id = s.id AND n.assessment_id = s.assessment_id
  AND s.code = 'C'  AND s.assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' LIMIT 1);

UPDATE norm n
SET mean = 8.567, sd = 4.5
FROM scale s
WHERE n.scale_id = s.id AND n.assessment_id = s.assessment_id
  AND s.code = 'ST' AND s.assessment_id = (SELECT id FROM assessment WHERE name = 'TCI 검사' LIMIT 1);
