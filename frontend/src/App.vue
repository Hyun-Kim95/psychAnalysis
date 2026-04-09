<template>
  <div id="app-root">
    <header class="app-header">
      <h1 class="app-title">{{ t('appTitle') }}</h1>
      <p class="app-subtitle">{{ t('appSubtitle') }}</p>
      <div class="lang-switch">
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
    </header>

    <nav v-if="isAdminRoute" class="app-nav">
      <router-link to="/" class="nav-btn">{{ t('goTestHome') }}</router-link>
    </nav>

    <main class="app-main">
      <router-view />
    </main>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useI18n } from './i18n'

const route = useRoute()
const isAdminRoute = computed(() => route.path === '/admin')
const { locale, setLocale, t } = useI18n()
</script>

<style scoped>
.lang-switch {
  margin-top: 8px;
  display: inline-flex;
  gap: 6px;
}
.lang-btn {
  border: 1px solid var(--border-color, #d0d7de);
  background: #fff;
  color: #334155;
  border-radius: 999px;
  padding: 4px 10px;
  font-size: 12px;
  cursor: pointer;
}
.lang-btn.active {
  background: #0f172a;
  color: #fff;
  border-color: #0f172a;
}
</style>
