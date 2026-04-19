<template>
  <div id="app-root">
    <header class="pa-topnav" aria-label="Main">
      <div class="pa-topnav-inner">
        <router-link to="/" class="pa-brand">{{ t('appTitle') }}</router-link>

        <div class="pa-topnav-actions">
          <div class="lang-switch lang-switch--nav" role="group" aria-label="Language">
            <button
              type="button"
              class="lang-btn"
              :class="{ active: locale === 'ko' }"
              @click="setLocale('ko')"
            >
              {{ t('languageKorean') }}
            </button>
            <button
              type="button"
              class="lang-btn"
              :class="{ active: locale === 'en' }"
              @click="setLocale('en')"
            >
              {{ t('languageEnglish') }}
            </button>
          </div>
          <router-link v-if="showTopAdminNav" to="/admin" class="pa-nav-admin-btn">
            {{ t('navAdminLogin') }}
          </router-link>
          <router-link v-if="isAdminRoute" to="/" class="pa-nav-secondary-btn">{{ t('goTestHome') }}</router-link>
        </div>
      </div>
    </header>

    <main class="app-main">
      <router-view />
    </main>

    <footer v-if="!isAdminRoute" class="pa-app-footer" aria-label="Site">
      <div class="pa-app-footer-inner">
        <router-link to="/board" class="pa-app-footer-link">{{ t('navBoard') }}</router-link>
        <span class="pa-app-footer-sep" aria-hidden="true">·</span>
        <router-link to="/info" class="pa-app-footer-link">{{ t('navSupportInfo') }}</router-link>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useI18n } from './i18n'
import { refreshSeoFromLastRoute } from './seo/documentSeo'

const route = useRoute()
const isAdminRoute = computed(() => route.path === '/admin')
/** 홈·문의 게시판·후원 안내에서는 상단 관리자 링크 숨김 (검사/결과 흐름에서만 노출) */
const hideTopAdminNav = computed(
  () => route.path === '/' || route.path === '/info' || route.path.startsWith('/board'),
)
const showTopAdminNav = computed(() => !isAdminRoute.value && !hideTopAdminNav.value)
const { locale, setLocale, t } = useI18n()

watch(locale, () => {
  refreshSeoFromLastRoute()
})
</script>

<style scoped>
.lang-switch {
  display: inline-flex;
  gap: 6px;
  align-items: center;
}
.lang-switch--nav {
  margin-top: 0;
}
.lang-btn {
  border: 1px solid var(--pa-border, #e6e8ea);
  background: var(--pa-bg-elevated, #fff);
  color: var(--pa-text-muted, #64748b);
  border-radius: 999px;
  padding: 4px 10px;
  font-size: 12px;
  cursor: pointer;
  font-family: var(--pa-font-heading, system-ui, sans-serif);
  font-weight: 500;
}
.lang-btn.active {
  background: var(--pa-brand, #00288e);
  color: #fff;
  border-color: var(--pa-brand, #00288e);
}

.pa-app-footer {
  border-top: 1px solid var(--pa-border, #e6e8ea);
  background: var(--pa-bg-footer, #f8fafc);
  padding: 12px 16px;
}

.pa-app-footer-inner {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  gap: 8px;
}

.pa-app-footer-sep {
  color: var(--pa-text-muted, #64748b);
  user-select: none;
}

.pa-app-footer-link {
  min-height: 44px;
  display: inline-flex;
  align-items: center;
  font-size: 14px;
  font-weight: 600;
  color: var(--pa-brand, #00288e);
  text-decoration: none;
}

.pa-app-footer-link:hover {
  text-decoration: underline;
}
</style>
