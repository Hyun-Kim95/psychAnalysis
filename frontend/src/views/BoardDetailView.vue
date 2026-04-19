<template>
  <div class="pa-board-detail">
    <div v-if="loading" class="card pa-board-detail-state">
      <p>{{ t('boardLoadingList') }}</p>
    </div>
    <div v-else-if="error" class="card card-error pa-board-detail-state">
      <p>{{ error }}</p>
    </div>

    <div v-else-if="phase === 'legacy'" class="card pa-board-detail-state">
      <h1 class="pa-board-detail-title">{{ summary?.title }}</h1>
      <p class="pa-board-detail-meta">{{ summaryMeta }}</p>
      <p class="pa-board-legacy-msg">{{ t('boardLegacyNoPasswordBody') }}</p>
    </div>

    <div v-else-if="phase === 'unlock'" class="card pa-board-detail-state pa-board-unlock">
      <h1 class="pa-board-detail-title">{{ summary?.title }}</h1>
      <p class="pa-board-detail-meta">{{ summaryMeta }}</p>
      <h2 class="pa-board-unlock-heading">{{ t('boardUnlockTitle') }}</h2>
      <p class="pa-board-unlock-desc">{{ t('boardUnlockDesc') }}</p>
      <form class="pa-board-unlock-form" @submit.prevent="onUnlock">
        <input
          v-model="unlockPassword"
          type="password"
          class="pa-board-input"
          autocomplete="current-password"
          maxlength="128"
          :aria-label="t('boardPasswordLabel')"
        />
        <p v-if="unlockError" class="pa-board-error">{{ unlockError }}</p>
        <button type="submit" class="primary-btn" :disabled="unlocking">
          {{ unlocking ? t('boardUnlockUnlocking') : t('boardUnlockSubmit') }}
        </button>
      </form>
    </div>

    <article v-else-if="phase === 'content' && post" class="card pa-board-detail-article">
      <h1 class="pa-board-detail-title">{{ post.title }}</h1>
      <p class="pa-board-detail-meta">{{ formatMeta(post) }}</p>
      <div class="pa-board-detail-body">{{ post.body }}</div>
      <section v-if="post.adminReply?.trim()" class="pa-board-admin-reply" aria-labelledby="board-admin-reply-h">
        <h2 id="board-admin-reply-h" class="pa-board-admin-reply-title">{{ t('boardAdminReplyHeading') }}</h2>
        <p class="pa-board-admin-reply-meta" v-if="post.adminRepliedAt">{{ formatDate(post.adminRepliedAt) }}</p>
        <div class="pa-board-admin-reply-body">{{ post.adminReply }}</div>
      </section>
    </article>

    <p class="pa-board-detail-nav">
      <router-link to="/board" class="pa-board-detail-link">{{ t('boardBackToList') }}</router-link>
      ·
      <router-link to="/board/new" class="pa-board-detail-link">{{ t('boardNewPost') }}</router-link>
    </p>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { fetchBoardPostSummary, unlockBoardPost, type BoardPostDetail, type BoardPostSummary } from '../api'
import { useI18n } from '../i18n'

type Phase = 'loading' | 'legacy' | 'unlock' | 'content'

const route = useRoute()
const { t, locale } = useI18n()
const loading = ref(true)
const error = ref<string | null>(null)
const phase = ref<Phase>('loading')
const summary = ref<BoardPostSummary | null>(null)
const post = ref<BoardPostDetail | null>(null)
const unlockPassword = ref('')
const unlockError = ref<string | null>(null)
const unlocking = ref(false)

const id = computed(() => (route.params.id as string) || '')

const summaryMeta = computed(() => {
  const s = summary.value
  if (!s) return ''
  const who = s.authorDisplay?.trim() || t('boardAuthorAnon')
  const d = formatDate(s.createdAt)
  return `${d} · ${who}`
})

function readInitialFromSession(): BoardPostDetail | null {
  if (!id.value) return null
  try {
    const raw = sessionStorage.getItem(`psych-board-init:${id.value}`)
    if (!raw) return null
    sessionStorage.removeItem(`psych-board-init:${id.value}`)
    const parsed = JSON.parse(raw) as BoardPostDetail
    if (parsed?.id === id.value) return parsed
  } catch {
    /* ignore */
  }
  return null
}

function formatMeta(p: BoardPostDetail) {
  const who = p.authorDisplay?.trim() || t('boardAuthorAnon')
  const d = formatDate(p.createdAt)
  return `${d} · ${who}`
}

function formatDate(iso: string) {
  try {
    const d = new Date(iso)
    return d.toLocaleString(locale.value === 'en' ? 'en-US' : 'ko-KR', {
      dateStyle: 'medium',
      timeStyle: 'short',
    })
  } catch {
    return iso
  }
}

async function load() {
  if (!id.value) {
    error.value = t('boardDetailLoadError')
    phase.value = 'loading'
    post.value = null
    summary.value = null
    loading.value = false
    return
  }

  const initial = readInitialFromSession()
  if (initial) {
    post.value = initial
    phase.value = 'content'
    summary.value = null
    error.value = null
    loading.value = false
    return
  }

  loading.value = true
  error.value = null
  unlockError.value = null
  post.value = null
  try {
    const s = await fetchBoardPostSummary(id.value)
    summary.value = s
    if (s.requiresPassword) {
      phase.value = 'unlock'
    } else {
      phase.value = 'legacy'
    }
  } catch {
    error.value = t('boardDetailLoadError')
    summary.value = null
    phase.value = 'loading'
  } finally {
    loading.value = false
  }
}

async function onUnlock() {
  unlockError.value = null
  if (!unlockPassword.value.trim()) {
    unlockError.value = t('boardValidationPassword')
    return
  }
  unlocking.value = true
  try {
    post.value = await unlockBoardPost(id.value, unlockPassword.value)
    phase.value = 'content'
    summary.value = null
  } catch {
    unlockError.value = t('boardUnlockError')
  } finally {
    unlocking.value = false
  }
}

onMounted(() => {
  void load()
})

watch(id, () => {
  unlockPassword.value = ''
  void load()
})
</script>

<style scoped>
.pa-board-detail {
  max-width: 720px;
  margin: 0 auto;
  padding: 24px 16px 48px;
}

.pa-board-detail-state {
  padding: 20px;
}

.pa-board-detail-article {
  padding: 22px 20px;
}

.pa-board-detail-title {
  font-family: var(--pa-font-heading);
  font-size: clamp(1.2rem, 3vw, 1.5rem);
  margin: 0 0 8px;
  color: var(--pa-text-strong);
}

.pa-board-detail-meta {
  margin: 0 0 20px;
  font-size: 13px;
  color: var(--pa-text-muted);
}

.pa-board-detail-body {
  white-space: pre-wrap;
  word-break: break-word;
  font-size: 15px;
  line-height: 1.6;
  color: var(--pa-text-body);
}

.pa-board-legacy-msg {
  font-size: 15px;
  line-height: 1.6;
  color: var(--pa-text-body);
  margin: 0;
}

.pa-board-unlock-heading {
  font-size: 1rem;
  margin: 20px 0 8px;
  color: var(--pa-text-strong);
}

.pa-board-unlock-desc {
  font-size: 14px;
  color: var(--pa-text-muted);
  margin: 0 0 12px;
}

.pa-board-unlock-form {
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-width: 320px;
}

.pa-board-input {
  font-family: var(--pa-font-body);
  font-size: 15px;
  padding: 10px 12px;
  border: 1px solid var(--pa-border);
  border-radius: var(--pa-radius-sm);
  background: var(--pa-bg-elevated);
  color: var(--pa-text-body);
}

.pa-board-error {
  color: #b45309;
  font-size: 14px;
  margin: 0;
}

.pa-board-admin-reply {
  margin-top: 28px;
  padding-top: 20px;
  border-top: 1px solid var(--pa-border);
}

.pa-board-admin-reply-title {
  font-size: 1rem;
  margin: 0 0 8px;
  color: var(--pa-text-strong);
}

.pa-board-admin-reply-meta {
  font-size: 12px;
  color: var(--pa-text-muted);
  margin: 0 0 10px;
}

.pa-board-admin-reply-body {
  white-space: pre-wrap;
  word-break: break-word;
  font-size: 15px;
  line-height: 1.6;
  color: var(--pa-text-body);
}

.pa-board-detail-nav {
  margin-top: 24px;
  text-align: center;
  font-size: 14px;
}

.pa-board-detail-link {
  font-weight: 600;
  color: var(--pa-brand);
  text-decoration: none;
}
</style>
