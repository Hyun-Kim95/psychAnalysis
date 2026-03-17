/**
 * Search Console 소유권 검증 파일을 해당 경로에서 직접 응답합니다.
 * rewrite보다 먼저 실행되어, 정적 파일이 index.html로 덮이지 않도록 합니다.
 */
export const config = {
  matcher: [
    '/google33b21a72f2e22b5e.html',
    '/naver1138a47f1ef4a4a630e04dfa7e7a5473.html',
  ],
};

export default function middleware(request: Request) {
  const pathname = new URL(request.url).pathname;

  if (pathname === '/google33b21a72f2e22b5e.html') {
    return new Response(
      'google-site-verification: google33b21a72f2e22b5e.html\n',
      {
        status: 200,
        headers: { 'Content-Type': 'text/html; charset=utf-8' },
      }
    );
  }

  if (pathname === '/naver1138a47f1ef4a4a630e04dfa7e7a5473.html') {
    return new Response(
      'naver-site-verification: naver1138a47f1ef4a4a630e04dfa7e7a5473.html\n',
      {
        status: 200,
        headers: { 'Content-Type': 'text/html; charset=utf-8' },
      }
    );
  }

  return new Response(null, { status: 404 });
}
