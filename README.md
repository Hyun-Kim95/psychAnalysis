# psychAnalysis

심리검사 분석 서비스입니다. 여러 종류의 심리검사를 실시하고, 결과를 T점수·규준 기준으로 해석하며, 관리자 대시보드와 PDF 리포트를 제공합니다.

## 기술 스택

- **백엔드**: Java 17, Spring Boot 4.x, PostgreSQL, Flyway
- **프론트엔드**: Vue 3, TypeScript, Vite, Vue Router, Chart.js, Axios
- **PDF**: Apache PDFBox

## 지원 검사

- TCI (기질·성격 검사)
- NEO 성격검사
- BDI 우울검사
- BAI 불안검사
- 회복탄력성 검사  

(간단/상세 버전이 있는 검사는 각각 별도로 구성됩니다.)

## 사전 요구사항

- **Java 17**
- **Node.js** (npm)
- **PostgreSQL**  
  - DB: `psychdb`  
  - 사용자: `psychuser` / 비밀번호: `psychpass`  
  - 포트: `5432` (기본값)

DB·계정은 `src/main/resources/application.properties`에서 변경할 수 있습니다.

## 실행 방법

### 1. 데이터베이스

PostgreSQL에서 DB와 사용자를 생성한 뒤, 애플리케이션 실행 시 Flyway가 마이그레이션을 적용합니다.

```sql
CREATE DATABASE psychdb;
CREATE USER psychuser WITH PASSWORD 'psychpass';
GRANT ALL PRIVILEGES ON DATABASE psychdb TO psychuser;
```

### 2. 백엔드

프로젝트 루트에서:

```bash
./mvnw spring-boot:run
```

기본 포트: **8080**

### 3. 프론트엔드

```bash
cd frontend
npm install
npm run dev
```

개발 서버: **http://localhost:5173**  
API 요청은 Vite 프록시를 통해 `http://localhost:8080`으로 전달됩니다.

글로벌 스타일은 Figma 시안에 맞춘 CSS 변수(`--pa-*`)로 관리합니다. 토큰·프레임 매핑은 [docs/ui-spec.md](docs/ui-spec.md)를 참고하세요. 웹폰트는 **Manrope**, **Public Sans**(Google Fonts)를 사용합니다.

홈 히어로 우측 이미지는 `frontend/public/images/home-hero.png`에 두며, Figma에서 직접 PNG를 뽑아 교체해도 됩니다(동일 경로·권장 비율 약 584×500).

### 4. 프로덕션 빌드

- 백엔드: `./mvnw clean package` → `target/*.jar` 실행
- 프론트: `cd frontend && npm run build` → `frontend/dist`를 정적 리소스로 서빙

## 주요 기능

- **사용자**: 검사 선택 → 문항 응답 제출 → 결과 요약·척도별 점수·해석·PDF 다운로드
- **관리자** (로그인 필요):
  - 대시보드: 검사 완료 수, 결과 수, 신뢰도(α), 통계 그래프, 접속 로그, 응답 목록(페이지네이션)
  - 검사별 **기준점수(규준)** 및 **해석 기준** 조회
  - **대시보드 요약 PDF**: 요약 테이블 + 화면 캡처 그래프(2×2)
  - **기준·해석 PDF**: 검사별 규준 테이블 + 해석 문단

## 다국어 지원 (ko/en)

- UI 언어는 상단 토글에서 **한국어/English** 전환이 가능합니다.
- 프론트엔드는 선택한 언어를 `Accept-Language` 헤더로 전송합니다.
- 백엔드는 다음 항목을 locale 기반으로 응답합니다:
  - 검사명, 척도명, 결과 해석 문구, 관리자 기준/해석 가이드, 선택지 라벨
- 문항 본문은 현재 다음 범위에서 영어가 제공됩니다:
  - BDI/BAI
  - NEO 성격검사(간단)
  - NEO 성격검사(상세)
  - TCI 검사(간단)
  - TCI 검사(상세)
  - 회복탄력성 검사(간단/상세)
- 그 외 문항은 원문(국문) fallback이 적용됩니다.

## 프로젝트 구조

```
psychAnalysis/
├── frontend/                 # Vue 3 + Vite 프론트엔드
│   ├── src/
│   │   ├── views/            # Intro, Test, Result, Admin 등
│   │   ├── api.ts            # API 호출
│   │   └── router/
│   └── package.json
├── src/main/java/.../
│   ├── admin/                # 관리자 인증, 대시보드, PDF, 기준·해석 API
│   ├── assessment/          # 검사·척도·규준·해석(Interpretation) 로직
│   ├── response/             # 응답 세션, 채점, 결과, PDF
│   └── common/
├── src/main/resources/
│   ├── application.properties
│   ├── db/migration/         # Flyway SQL
│   └── fonts/               # PDF 한글 폰트
└── pom.xml
```

## License

Licensed under the [MIT License](LICENSE).
