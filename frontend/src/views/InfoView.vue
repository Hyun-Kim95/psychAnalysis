<template>
  <div class="pa-info-page">
    <div class="pa-info-inner">
      <header class="pa-info-header">
        <h1 class="pa-info-title">{{ t('infoPageTitle') }}</h1>
        <p class="pa-info-lead">{{ t('infoPageLead') }}</p>
      </header>

      <section class="pa-info-section card" aria-labelledby="info-donation-heading">
        <h2 id="info-donation-heading" class="pa-info-section-title">{{ t('infoDonationTitle') }}</h2>
        <div class="pa-info-donation-text">
          <p class="pa-info-section-body">{{ t('infoDonationBodyLine1') }}</p>
          <p class="pa-info-section-body">{{ t('infoDonationBodyLine2') }}</p>
        </div>
        <div class="pa-info-notice" role="note">
          <p class="pa-info-notice-line">{{ t('infoDonationNoticeLine1') }}</p>
          <p class="pa-info-notice-line">{{ t('infoDonationNoticeLine2') }}</p>
        </div>
        <p v-if="donationUrl" class="pa-info-actions">
          <a
            :href="donationUrl"
            class="pa-info-link-btn"
            target="_blank"
            rel="noopener noreferrer"
          >
            {{ t('infoDonationLink') }}
          </a>
        </p>
        <p v-else class="pa-info-muted">{{ t('infoDonationUnavailable') }}</p>
      </section>

      <section class="pa-info-section card" aria-labelledby="info-disclaimer-heading">
        <h2 id="info-disclaimer-heading" class="pa-info-section-title">{{ t('infoDisclaimerTitle') }}</h2>
        <p class="pa-info-section-body">{{ t('infoDisclaimerBody') }}</p>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onBeforeUnmount, onMounted, watch } from 'vue'
import { locale, useI18n } from '../i18n'
import { donationUrl, siteOrigin } from '../siteConfig'

const { t } = useI18n()

const JSON_LD_ID = 'pa-jsonld-website'

function baseUrlForJsonLd(): string {
  if (siteOrigin) return siteOrigin
  if (typeof window !== 'undefined') return window.location.origin
  return ''
}

function injectJsonLd() {
  if (typeof document === 'undefined') return
  const base = baseUrlForJsonLd()
  if (!base) return
  let el = document.getElementById(JSON_LD_ID) as HTMLScriptElement | null
  if (!el) {
    el = document.createElement('script')
    el.id = JSON_LD_ID
    el.type = 'application/ld+json'
    document.head.appendChild(el)
  }
  const name = t('appTitle')
  const desc = t('metaDescHome')
  el.textContent = JSON.stringify({
    '@context': 'https://schema.org',
    '@type': 'WebSite',
    name,
    description: desc,
    url: `${base}/`,
  })
}

function removeJsonLd() {
  document.getElementById(JSON_LD_ID)?.remove()
}

onMounted(() => {
  injectJsonLd()
})

watch(locale, () => injectJsonLd())

onBeforeUnmount(() => {
  removeJsonLd()
})
</script>

<style scoped>
.pa-info-page {
  min-height: 60vh;
  padding: 24px 16px 48px;
  background: var(--pa-bg-page);
}

.pa-info-inner {
  max-width: 640px;
  margin: 0 auto;
}

.pa-info-header {
  margin-bottom: 24px;
}

.pa-info-title {
  font-family: var(--pa-font-heading);
  font-size: clamp(1.5rem, 4vw, 2rem);
  font-weight: 700;
  color: var(--pa-text-strong);
  margin: 0 0 8px;
}

.pa-info-lead {
  margin: 0;
  color: var(--pa-text-muted);
  font-size: 15px;
}

.pa-info-section {
  margin-bottom: 16px;
  padding: 20px;
}

.pa-info-section-title {
  font-family: var(--pa-font-heading);
  font-size: 1.125rem;
  margin: 0 0 10px;
  color: var(--pa-text-strong);
}

.pa-info-section-body {
  margin: 0 0 12px;
  font-size: 15px;
  line-height: 1.55;
}

.pa-info-donation-text {
  margin-bottom: 12px;
}

.pa-info-donation-text .pa-info-section-body {
  margin-bottom: 6px;
}

.pa-info-donation-text .pa-info-section-body:last-child {
  margin-bottom: 0;
}

.pa-info-notice {
  margin: 0 0 14px;
  padding: 10px 12px;
  background: var(--pa-accent-surface);
  border-radius: var(--pa-radius-sm);
  border: 1px solid var(--pa-accent-border);
}

.pa-info-notice-line {
  margin: 0;
  font-size: 13px;
  line-height: 1.5;
  color: var(--pa-text-muted);
}

.pa-info-notice-line + .pa-info-notice-line {
  margin-top: 6px;
}

.pa-info-muted {
  margin: 0;
  font-size: 14px;
  color: var(--pa-text-muted);
}

.pa-info-actions {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.pa-info-link-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 44px;
  padding: 0 18px;
  border-radius: var(--pa-radius-sm);
  font-weight: 600;
  font-size: 14px;
  text-decoration: none;
  background: var(--pa-brand);
  color: #fff;
  border: 1px solid var(--pa-brand);
}

.pa-info-link-btn--secondary {
  background: var(--pa-bg-elevated);
  color: var(--pa-brand);
}
</style>
