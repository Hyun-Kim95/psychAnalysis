# 프론트엔드 계획: Figma 스타일 1차 정렬

**범위**: 시각 토큰(색·폰트·반경)·글로벌 컴포넌트 스타일·차트 색상 톤. 라우팅·API·문구(i18n 키) 변경 없음.

---

## 1. 변경 파일 (예상)

| 파일 | 작업 |
|------|------|
| `frontend/index.html` | Manrope, Public Sans 웹폰트 링크 추가 |
| `frontend/src/style.css` | `:root` 토큰 재정의, 하드코딩 색을 변수 참조로 치환, `#app` 최대 너비 1280px 정렬 |
| `frontend/src/App.vue` | 언어 버튼 스타일을 토큰 기반으로 정리 |
| `frontend/src/views/ResultView.vue` | `tScoreBarColor` 및 50점 그리드 색을 브랜드 톤으로 조정 |
| `frontend/src/views/AdminDashboardView.vue` | 차트 `backgroundColor`를 브랜드/보조 팔레트로 조정 (선택적 일관성) |

---

## 2. 구현 순서

1. 폰트 로드 및 `:root` `--pa-*` + 별칭 변수 추가.
2. `body`, 제목, 링크, 버튼, 카드, 진행, 선택지, 테이블, 결과 블록 순으로 기존 HEX 제거.
3. `App.vue` scoped 스타일 업데이트.
4. Chart.js 색상 함수 및 상수 치환.
5. `npm run build`로 타입·번들 검증.

---

## 3. 비범위

- Figma와 픽셀 단위 동일 레이아웃(1280 고정 디자인) 재현.
- 다크 모드 토글 UI (`.dark` 변수만 예비 유지).
- PDF/백엔드 렌더링 색상.

---

## 4. 레이아웃 매칭 (Figma 프레임 ↔ 화면)

- 상세 표·체크리스트: **`docs/ui-spec.md` §8**.
- 구현 요약: `App.vue` 상단 바(브랜드·언어·관리자), `AssessmentListView.vue` **2열 히어로**(일러스트 `/public/images/home-hero.png`)·라이브러리·푸터, `TestView.vue` Figma 진행 화면(`.pa-test-page`·헤더·pill 진행 바·질문 카드·선지 타일/스택·텍스트형 이전), `ResultView.vue` 브레드크럼·헤더 액션, `IntroView.vue` 폭 정렬.

## 5. 검증

- 검사 목록 → 문항 → 결과 요약까지 스크롤하며 대비·포커스 링 가독성 확인.
- 상단 네비·`/#pa-library` 앵커 스크롤 동작 확인.
- 관리자 대시보드 차트 가독성 확인.
- 모바일 너비에서 카드 라운딩·네비 접기 확인.
