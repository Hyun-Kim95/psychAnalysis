# 게이트 체크리스트 (psychAnalysis)

작업 완료 전 최소 확인 항목.

## 빌드·테스트

- [x] `frontend`에서 `npm run build` 성공 (2026-04-10)
- [ ] `npx vitest run` — 현재 axios 모킹 이슈로 스위트 수집 실패(기존). `reports/test-report.md` 참고.

## UI (본 변경과 관련)

- [ ] 전역 배경·본문색이 Figma 토큰(`#f7f9fb` / `#444653`)과 일관
- [ ] Primary 버튼·진행 바·활성 네비가 `#00288e` 계열
- [ ] 언어 전환 버튼 활성/비활성 상태가 명확
- [ ] 모바일에서 카드 라운딩이 과하지 않음

## 문서

- [ ] `docs/ui-spec.md`와 실제 CSS 변수가 크게 어긋나지 않음
