# 테스트 보고 — Figma 스타일 정렬 (2026-04-10)

## 자동

| 항목 | 결과 | 비고 |
|------|------|------|
| `frontend`: `npm run build` (`tsc && vite build`) | 통과 | |
| `frontend`: `npx vitest run` | 실패 | 수집 단계에서 `axios.interceptors`가 undefined — 스타일 변경과 무관한 기존 이슈로 보임 (`src/api.ts:9`) |
| `backend`: `mvnw compile -DskipTests` (`ResultPdfService` / `PdfLocaleStrings`) | 통과 | 2026-04-10 |

## 수동 (권장)

1. `cd frontend && npm run dev` 후 검사 목록·카드 라운딩·호버 보더 확인.
2. 문항 화면: `.pa-test-page` 배경·헤더(eyebrow + 진행 문구 + n/m)·pill 진행 바·질문 카드 보더·선지 2–6개 짧은 라벨 시 타일 그리드·하단 텍스트형 이전(`pa-test-back-link`)·Primary 다음 버튼 확인.
3. 결과 화면: 제목 색·차트 막대 색(낮음/보통/높음 T) 대비 확인.
4. 관리자 대시보드: 네 차트 색상이 서로 구분되는지 확인.
5. 상단 언어 토글: 활성 = 브랜드 채움, 비활성 = 뮤트드 텍스트.
6. 결과 PDF (척도 5개 이상 검사): 다운로드 후 T점수 막대 위에 「차원 매트릭스」제목·힌트·원형 레이더(규준 50 점선·채움 프로파일)·꼭짓점 척도 코드 라벨이 보이는지, 이어서 기존 가로 막대 패널이 오는지 확인.

## 결론

빌드는 통과했습니다. Vitest는 기존 환경/모킹 문제로 스위트가 로드되지 않았으며, 본 변경의 회귀 여부는 위 수동 절차로 보완하는 것이 좋습니다.
