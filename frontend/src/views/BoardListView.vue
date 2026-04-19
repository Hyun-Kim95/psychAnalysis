<template>
  <div class="pa-board">
    <header class="pa-board-header">
      <h1 class="pa-board-title">{{ t('boardPageTitle') }}</h1>
      <router-link to="/board/new" class="pa-board-primary-btn">{{ t('boardNewPost') }}</router-link>
    </header>

    <div v-if="loading" class="card pa-board-state">
      <p>{{ t('boardLoadingList') }}</p>
    </div>
    <div v-else-if="error" class="card card-error pa-board-state">
      <p>{{ error }}</p>
    </div>
    <div v-else-if="page && page.content.length === 0" class="card pa-board-state">
      <p>{{ t('boardEmpty') }}</p>
      <router-link to="/board/new" class="pa-board-link">{{ t('boardNewPost') }}</router-link>
    </div>
    <div v-else-if="page" class="pa-board-list card">
      <ul class="pa-board-items">
        <li v-for="row in page.content" :key="row.id" class="pa-board-item">
          <router-link :to="`/board/${row.id}`" class="pa-board-item-link">
            <span class="pa-board-item-left">
              <span class="pa-board-item-title">{{ row.title }}</span>
            </span>
            <span class="pa-board-item-right">
              <span
                class="pa-board-item-reply"
                :class="row.hasAdminReply ? 'pa-board-item-reply--done' : 'pa-board-item-reply--pending'"
              >
                {{ row.hasAdminReply ? t('boardReplyDone') : t('boardReplyPending') }}
              </span>
              <span class="pa-board-item-meta">{{ formatMeta(row) }}</span>
            </span>
          </router-link>
        </li>
      </ul>
      <nav v-if="page.totalPages > 1" class="pa-board-pager" aria-label="Pagination">
        <button
          type="button"
          class="secondary-btn"
          :disabled="page.number <= 0"
          @click="goPage(page.number - 1)"
        >
          {{ t('boardPrevPage') }}
        </button>
        <span class="pa-board-page-num">{{ page.number + 1 }} / {{ page.totalPages }}</span>
        <button
          type="button"
          class="secondary-btn"
          :disabled="page.number >= page.totalPages - 1"
          @click="goPage(page.number + 1)"
        >
          {{ t('boardNextPage') }}
        </button>
      </nav>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { fetchBoardPosts, type BoardPostPage } from '../api'
import { useI18n } from '../i18n'

const { t, locale } = useI18n()
const loading = ref(true)
const error = ref<string | null>(null)
const page = ref<BoardPostPage | null>(null)
const pageSize = 10

function formatMeta(row: { authorDisplay: string | null; createdAt: string }) {
  const who = row.authorDisplay?.trim() || t('boardAuthorAnon')
  const d = formatDate(row.createdAt)
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

async function load(p: number) {
  loading.value = true
  error.value = null
  try {
    page.value = await fetchBoardPosts(p, pageSize)
  } catch (e: unknown) {
    error.value = t('boardLoadError')
    if (import.meta.env.DEV) {
      console.warn('[board] fetchBoardPosts failed', e)
    }
    page.value = null
  } finally {
    loading.value = false
  }
}

function goPage(p: number) {
  void load(p)
}

onMounted(() => {
  void load(0)
})
</script>

<style scoped>
.pa-board {
  max-width: 720px;
  margin: 0 auto;
  padding: 24px 16px 48px;
}

.pa-board-header {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 20px;
}

.pa-board-title {
  font-family: var(--pa-font-heading);
  font-size: clamp(1.35rem, 3.5vw, 1.75rem);
  margin: 0;
  color: var(--pa-text-strong);
}

.pa-board-primary-btn {
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
}

.pa-board-state {
  padding: 20px;
}

.pa-board-link {
  display: inline-block;
  margin-top: 12px;
  font-weight: 600;
  color: var(--pa-brand);
}

.pa-board-list {
  padding: 0;
  overflow: hidden;
}

.pa-board-items {
  list-style: none;
  margin: 0;
  padding: 0;
}

.pa-board-item {
  border-bottom: 1px solid var(--pa-border);
}

.pa-board-item:last-child {
  border-bottom: none;
}

.pa-board-item-link {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: baseline;
  gap: 8px 16px;
  padding: 14px 18px;
  text-decoration: none;
  color: inherit;
}

.pa-board-item-link:hover {
  background: var(--pa-bg-section);
}

.pa-board-item-left {
  flex: 1 1 12rem;
  min-width: 0;
}

.pa-board-item-title {
  display: block;
  font-weight: 700;
  color: var(--pa-text-strong);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.pa-board-item-right {
  display: flex;
  flex-wrap: wrap;
  align-items: baseline;
  justify-content: flex-end;
  gap: 8px 12px;
  flex: 0 1 auto;
  text-align: right;
  max-width: 100%;
}

.pa-board-item-reply {
  font-size: 12px;
  font-weight: 600;
  white-space: nowrap;
}

.pa-board-item-reply--done {
  color: #166534;
}

.pa-board-item-reply--pending {
  color: var(--pa-text-muted);
}

.pa-board-item-meta {
  font-size: 12px;
  color: var(--pa-text-muted);
  line-height: 1.35;
  text-align: right;
}

.pa-board-pager {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 16px;
  border-top: 1px solid var(--pa-border);
}

.pa-board-page-num {
  font-size: 14px;
  color: var(--pa-text-muted);
}
</style>
