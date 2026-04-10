# 리뷰 보고 — Figma 스타일 정렬 (2026-04-10)

## 범위

- `frontend/src/style.css`: 디자인 토큰(`--pa-*`) 도입, 글로벌 컴포넌트 색·반경·타이포 정리.
- `frontend/index.html`: Manrope / Public Sans 로드.
- `frontend/src/App.vue`: 언어 버튼을 토큰 기반으로 정렬.
- `ResultView.vue` / `AdminDashboardView.vue`: Chart.js 색상을 브랜드·보조 팔레트에 맞춤.
- 문서: `docs/research.md`, `docs/ui-spec.md`, `docs/frontend-plan.md`, `docs/gate-checklist.md`, `docs/prd.md` §1.4.
- **추가 (2026-04-10)**: `ResultPdfService` / `PdfLocaleStrings` — 웹 결과(척도≥5 레이더)와 PDF 내용 정렬.

## 긍정

- 단일 토큰 소스로 이전 혼재(oklch + 임의 Tailwind색)를 정리함.
- Figma에서 반복 확인된 `#00288e` / `#f7f9fb` 등이 일관되게 적용됨.
- `color-mix`는 최신 브라우저에서만 의미 있음; 미지원 시 근접한 단색 폴백은 제한적이나 핵심 색은 HEX 변수로 유지됨.

## 리스크·후속

- **브라우저**: `color-mix` 미지원 시 호버 보더 등이 약간 달라질 수 있음. 필요 시 `@supports`로 단순 보더색 폴백 추가 검토.
- **Figma 픽셀 일치**: 홈·히어로·상단 네비는 이전 라운드에서 정렬됨. 검사 진행(`TestView`)은 `3458:413`에 맞춰 페이지 래퍼·헤더 카피·진행 바·카드·선지 레이아웃·푸터 액션을 갱신함(중도 저장 UI 없음).
- **테스트**: Vitest 수집 실패는 별도 이슈로 추적 권장.
- **PDF**: 공간이 매우 부족하면 레이더 반경이 줄거나(최소 50pt 미만) 스킵될 수 있음. 대부분 A4·5~15척도에서는 표시됨.

## Blocker

- 없음 (프로덕션 빌드 성공 기준).

## Subagent

- 사용하지 않음. TalkToFigma MCP로 디자인 수치를 직접 조회했으며, 변경 범위가 단일 저장소·문서 세트로 한 에이전트 처리에 적합함.
