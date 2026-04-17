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
          <router-link v-if="!isAdminRoute && !isHomeRoute" to="/admin" class="pa-nav-admin-btn">
            {{ t('navAdminLogin') }}
          </router-link>
          <router-link v-else to="/" class="pa-nav-secondary-btn">{{ t('goTestHome') }}</router-link>
        </div>
      </div>
    </header>

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
const isHomeRoute = computed(() => route.path === '/')
const { locale, setLocale, t } = useI18n()
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
</style>
