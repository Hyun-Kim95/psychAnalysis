# UI 명세: Figma 정렬 디자인 토큰

**기준 문서**: Figma `Page 3` (Home / Assessment / Result / Admin)  
**구현 위치**: `frontend/src/style.css` (`:root`), `frontend/index.html` (폰트), 필요 시 뷰별 보조 클래스.

---

## 1. 원칙

- **단일 소스**: 색·반경·폰트 패밀리는 CSS 변수(`--pa-*`)로만 정의하고, 화면 클래스는 이를 참조한다.
- **브랜드**: 임상·전문 톤의 **딥 네이비** (`#00288e`). 범용 블루(`#2563eb` 등)로 대체하지 않는다.
- **가독성**: 본문 `#444653`, 제목 강조는 브랜드 네이비 또는 `#191c1e`를 용도에 맞게 구분한다.
- **반응형**: 벤토 카드 `32px` 라운딩은 **뷰포트 ≤640px**에서 `16px`로 낮춘다.

---

## 2. 색상 (`:root`)

| 변수 | 값 | 용도 |
|------|-----|------|
| `--pa-brand` | `#00288e` | Primary 버튼, 진행 바, 주요 제목, 링크 강조 |
| `--pa-brand-nav` | `#1e3a8a` | 로고/브랜드 워드마크, 네비 강조, 알림 보더 |
| `--pa-brand-gradient-end` | `#1e40af` | (선택) 그라데이션 종단 |
| `--pa-text-body` | `#444653` | 본문 |
| `--pa-text-strong` | `#191c1e` | 강한 본문·내부 제목 |
| `--pa-text-muted` | `#64748b` | 보조·푸터 |
| `--pa-text-label` | `#757684` | 폼/문항 보조 라벨 |
| `--pa-text-nav` | `#475569` | 2차 네비 비활성 |
| `--pa-text-breadcrumb` | `#94a3b8` | 경로·메타 |
| `--pa-bg-page` | `#f7f9fb` | 전역 배경 |
| `--pa-bg-section` | `#f2f4f6` | 섹션 구획 |
| `--pa-bg-footer` | `#f8fafc` | 푸터 바 |
| `--pa-bg-elevated` | `#ffffff` | 카드·모달 표면 |
| `--pa-border` | `#e6e8ea` | 기본 테두리·구분선 |
| `--pa-accent-surface` | `#dbeafe` | 정보·선택 하이라이트 배경 |
| `--pa-surface-subtle` | `#e6e8ea` | 보조 버튼 배경 |

**시맨틱 별칭** (기존 코드 호환): `--card-bg`, `--border-color`, `--text-secondary`, `--bg-notice`, `--border-notice` 등은 위 토큰에 매핑한다.

---

## 3. 타이포그래피

- **제목**: `Manrope`, weight 700–800, 브랜드/스트롱 컬러는 화면별로 `ui-spec` §2 준수.
- **본문·UI**: `Public Sans`, 13–16px 구간이 기본.
- **로드**: Google Fonts `Manrope` (400,500,600,700,800), `Public Sans` (400,500,600,700).

---

## 4. 반경

| 변수 | 값 |
|------|-----|
| `--pa-radius-sm` | 8px |
| `--pa-radius-md` | 12px |
| `--pa-radius-lg` | 24px |
| `--pa-radius-bento` | 32px (모바일 16px) |

---

## 5. 컴포넌트 매핑 (글로벌 클래스)

| 클래스 | 기대 |
|--------|------|
| `.primary-btn` | 배경 `--pa-brand`, 텍스트 흰색, 반경 `--pa-radius-sm` |
| `.secondary-btn` | 배경 `--pa-surface-subtle`, 텍스트 `--pa-text-strong` |
| `.nav-btn` / `.nav-btn.active` | 비활성: 연한 테두리·배경; 활성: 브랜드 톤 테두리·연한 브랜드 서피스 |
| `.assessment-card` | 표면 흰색, 반경 `--pa-radius-bento`, 호버 시 미세 보더 강화 |
| `.card` | 그림자는 유지하되 색 대비는 쿨 그레이 톤 |
| `.progress` / `.progress-bar` | 트랙 `--pa-border`, 채움 `--pa-brand` |
| `.choice-btn.selected` | 브랜드 보더 + `--pa-accent-surface` 배경 |

---

## 6. 언어 전환 (`App.vue`)

- 비활성: 텍스트 `--pa-text-muted`, 배경 투명/흰색, 테두리 `--pa-border`.
- 활성: 배경 `--pa-brand`, 텍스트 흰색, 테두리 `--pa-brand` (Figma의 Admin/KO 토글과 동일 계열).

---

## 7. 차트 (Chart.js)

- T점수 막대·기준선은 **인디고/오렌지 하드코딩** 대신 브랜드·슬레이트 톤의 RGBA로 조정한다.
- 상세 값은 `frontend/src/views/ResultView.vue`, `AdminDashboardView.vue` 내 주석과 함께 유지한다.

---

## 8. Figma 프레임 ↔ 라우트 ↔ Vue ↔ 섹션 매칭

**Figma 문서**: `Page 3` (노드 기준 프레임 이름·ID는 디자인 파일과 동일할 때 유효)

### 8.1 총괄 표

| Figma 프레임 | 프레임 노드 ID | 앱 경로 | 진입 컴포넌트 | 주요 하위 뷰 |
|----------------|----------------|---------|----------------|--------------|
| Home - Test Selection | `3458:520` | `/` | `UserFlowView.vue` | `AssessmentListView.vue` (목록 단계) |
| (검사 소개·세션 생성) | — (동일 사용자 플로우) | `/` (동일) | `UserFlowView.vue` | `IntroView.vue` |
| Assessment - In Progress | `3458:413` | `/` (동일) | `UserFlowView.vue` | `TestView.vue` |
| Result - Analysis Report | `3458:205` | `/result/:resultId` | `ResultPageView.vue` | `ResultView.vue` |
| Admin Dashboard | `3458:2` | `/admin` | `AdminFlowView.vue` | `AdminLoginView.vue` → `AdminDashboardView.vue` |

### 8.2 섹션 단위 체크리스트

아래는 **레이아웃·구획** 기준 매칭이다. 토큰(색·폰트)은 §1–§6.

#### A. `3458:520` Home → `/` + `AssessmentListView.vue`

| Figma 섹션 | 구현 위치 | 상태 |
|------------|-----------|------|
| Top Navigation Bar | `App.vue` (`.pa-topnav`) — 브랜드, 언어, 관리자(또는 검사 홈) | 완료 (중앙 텍스트 메뉴 없음) |
| Hero (2열: 카피 + 우측 일러스트, 블롭 장식, 보조 안내 문단) | `AssessmentListView.vue` (`.pa-hero-split`, `/public/images/home-hero.png`) | 완료 |
| Assessment Library (회색 배경·헤드행·카드 그리드) | `AssessmentListView.vue` (`#pa-library`, `.pa-library-section`) | 완료 |
| Informational 3열 | — | **미포함** (카피 전용 블록 제거) |
| Footer | `AssessmentListView.vue` (`.pa-site-footer`) | 완료 (서비스명·저작권 한 줄만) |

#### B. `3458:413` Assessment → `TestView.vue`

| Figma 섹션 | 구현 위치 | 상태 |
|------------|-----------|------|
| 페이지 배경·중앙 캔버스 | `.pa-test-page`, `.pa-test-inner` (최대 768px) | 완료 |
| 상단 eyebrow + 진행 문구 + n/m 카운터 | `.pa-test-eyebrow`, `.pa-test-progress-line`, `.pa-test-counter` | 완료 |
| 두꺼운 pill 진행 바 | `.pa-test-progress` + `.progress-bar` | 완료 |
| 질문 영역 (흰 카드·라운드·보더) | `.pa-question-card` | 완료 |
| 보기 버튼 | `.choices` / `.choice-btn` + `choiceLayout`: 짧은 2–6지 선지는 `.pa-choices--tiles`, 그 외 `.pa-choices--stack` | 완료 |
| 하단 이전(텍스트 링크형)·다음(Primary) | `.pa-test-back-link`, `.pa-test-next-btn` | 완료 (중도 저장 버튼 없음) |
| 안내 문구 | `.pa-test-hint` | 완료 |

#### C. `3458:205` Result → `ResultView.vue`

| Figma 섹션 | 구현 위치 | 상태 |
|------------|-----------|------|
| Breadcrumb (Home › …) | `.pa-result-breadcrumb` | 완료 |
| 페이지 제목 행 + 우측 Primary(PDF 등) | `.pa-result-header`, `.pa-result-actions--header` | 완료 |
| 차트·표·해석 본문 | 기존 카드 내부 | 완료 (벤토·사이드바 레이아웃은 미구현) |
| Dimensional matrix (척도 ≥5) | `ResultView.vue` — 레이더 차트(원형 그리드 `circular: true`), T 20~80·규준 50 점선 다각형·채움 프로파일, 배지 | 완료 |
| 동일 조건 PDF (척도 ≥5) | `ResultPdfService` — `drawDimensionalMatrixRadar`: 원형 격자·T 20~80·규준 50 점선·프로파일 채움 후 기존 T 막대 패널 | 완료 |
| 좌측 고정 사이드바 | — | 미구현 (응시자 결과 경로에서는 생략) |

#### D. `3458:2` Admin → `AdminDashboardView.vue`

| Figma 섹션 | 구현 위치 | 상태 |
|------------|-----------|------|
| 좌측 네비 사이드바 | — | 미구현 |
| 본문 카드·차트 그리드 | 기존 대시보드 | 토큰만 정렬됨, 프레임 단위 레이아웃은 추후 |

#### E. 검사 전 소개 `IntroView.vue`

| 비고 | 상태 |
|------|------|
| Figma 단독 프레임 없음 → Home의 카드형 CTA와 톤 맞춤 | `.pa-intro` 래퍼로 폭·정렬 정리 | 완료 |

### 8.3 유지보수 규칙

- Figma 프레임이 바뀌면 **본 표의 노드 ID·행**을 먼저 갱신한다.
- 새 화면이 추가되면 **8.1에 한 행 추가** 후 **8.2에 섹션 체크리스트**를 붙인다.
