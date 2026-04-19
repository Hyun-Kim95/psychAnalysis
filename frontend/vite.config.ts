import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'node:path'
import { writeFileSync } from 'node:fs'

function trimTrailingSlash(s: string): string {
  return s.replace(/\/+$/, '')
}

const defaultSiteOrigin =
  'https://psych-analysis-git-main-hyun-kim95s-projects.vercel.app'

export default defineConfig(({ mode, command }) => {
  const env = loadEnv(mode, process.cwd(), '')
  const siteOrigin = trimTrailingSlash(env.VITE_SITE_ORIGIN || defaultSiteOrigin)

  return {
    plugins: [
      vue(),
      {
        name: 'psych-seo-static',
        transformIndexHtml(html) {
          return html.replaceAll('__PSYCH_SITE_ORIGIN__', siteOrigin)
        },
        closeBundle() {
          if (command !== 'build') return
          const outDir = resolve(process.cwd(), 'dist')
          const robots = `User-agent: *\nAllow: /\nSitemap: ${siteOrigin}/sitemap.xml\n`
          const sitemap = `<?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
  <url>
    <loc>${siteOrigin}/</loc>
    <changefreq>weekly</changefreq>
    <priority>1.0</priority>
  </url>
  <url>
    <loc>${siteOrigin}/info</loc>
    <changefreq>monthly</changefreq>
    <priority>0.6</priority>
  </url>
  <url>
    <loc>${siteOrigin}/board</loc>
    <changefreq>daily</changefreq>
    <priority>0.7</priority>
  </url>
</urlset>
`
          writeFileSync(resolve(outDir, 'robots.txt'), robots)
          writeFileSync(resolve(outDir, 'sitemap.xml'), sitemap)
        },
      },
    ],
    server: {
      port: 5173,
      proxy: {
        '/api': {
          target: 'http://localhost:8080',
          changeOrigin: true,
        },
      },
    },
  }
})
