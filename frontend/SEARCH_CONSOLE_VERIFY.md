# Search Console 소유권 확인 실패 원인 및 해결

## 원인: Vercel 배포 보호(Deployment Protection)

`psych-analysis-git-main-hyun-kim95s-projects.vercel.app` 주소는 **Preview 배포** URL입니다.  
Vercel에서 **Deployment Protection**이 켜져 있으면, 이 주소로 접속할 때 **실제 사이트 대신 로그인 페이지**가 열립니다.

- Google이 이 URL을 요청하면 → **로그인/인증 페이지 HTML**만 받음  
- 우리가 넣은 `google-site-verification` 메타 태그는 **우리 앱의 index.html**에만 있음  
- 그래서 Google은 메타 태그를 **절대 볼 수 없고**, "사이트를 찾을 수 없습니다" / "메타 태그를 찾을 수 없음"으로 실패합니다.

---

## 해결 방법 (둘 중 하나 선택)

### 방법 1: **Production URL로 속성 추가** (권장)

1. Vercel 대시보드 → 해당 프로젝트 → **Settings** → **Domains**  
2. **Production 도메인** 확인 (예: `psych-analysis.vercel.app`)
3. Search Console에서 **기존 속성 삭제** 후, **새 속성 추가**  
   - **URL 접두어** 선택  
   - 주소: `https://psych-analysis.vercel.app` (Production 도메인 그대로 입력, 끝에 `/` 없이)
4. 검증 방법: **HTML 태그** 선택 후 **확인** 클릭  
   - 메타 태그는 이미 `index.html`에 있으므로 배포만 되어 있으면 됨.

Production 도메인은 보통 Deployment Protection 대상이 아니라서, Google이 실제 사이트 HTML을 받을 수 있습니다.

---

### 방법 2: **Preview URL을 공개로 예외 처리**

`psych-analysis-git-main-hyun-kim95s-projects.vercel.app` 를 그대로 속성 URL로 쓰고 싶다면:

1. Vercel 대시보드 → 해당 프로젝트 → **Settings** → **Deployment Protection**
2. **Protection Bypass for Automation** 또는 **Deployment Protection Exceptions** (플랜에 따라 이름 다름) 에서  
   - 해당 Preview URL을 **예외(Allowlist)** 에 추가하거나  
   - Protection 범위를 **Production만 보호**로 바꿔서 Preview는 공개되게 설정

또는:

- **Deployment Protection** → **Standard Protection** 인지 확인  
  - Hobby 플랜이면 Preview는 보호, **Production 도메인은 이미 공개**이므로, **방법 1**처럼 Production URL로 속성 추가하는 것이 가장 단순합니다.

---

## 확인 절차 (해결 후)

1. **시크릿 창**에서 속성으로 넣은 URL 열기 (로그인 없이)
2. **실제 앱(심리검사 서비스)** 가 보이면 OK. 로그인/비밀번호 화면이 보이면 아직 보호 걸려 있는 것.
3. **페이지 소스 보기(Ctrl+U)** → `google-site-verification` 검색 → 메타 태그가 보이면 Search Console에서 **확인** 클릭.
