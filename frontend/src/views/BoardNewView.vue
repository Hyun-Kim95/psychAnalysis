<template>
  <div class="pa-board-new">
    <h1 class="pa-board-new-title">{{ t('boardNewPost') }}</h1>

    <form class="card pa-board-form" @submit.prevent="onSubmit">
      <p class="pa-board-notice pa-board-notice--warn" role="note">
        {{ t('boardNoEditDeleteNotice') }}
      </p>
      <label class="pa-board-field">
        <span class="pa-board-label">{{ t('boardTitleLabel') }}</span>
        <input v-model.trim="title" type="text" class="pa-board-input" maxlength="200" required />
      </label>
      <label class="pa-board-field">
        <span class="pa-board-label">{{ t('boardBodyLabel') }}</span>
        <textarea
          v-model.trim="body"
          class="pa-board-textarea"
          rows="10"
          maxlength="8000"
          required
        />
      </label>
      <label class="pa-board-field">
        <span class="pa-board-label">{{ t('boardAuthorLabel') }}</span>
        <input v-model.trim="authorDisplay" type="text" class="pa-board-input" maxlength="64" />
        <span class="pa-board-hint">{{ t('boardAuthorHint') }}</span>
      </label>
      <label class="pa-board-field">
        <span class="pa-board-label">{{ t('boardPasswordLabel') }}</span>
        <input
          v-model="password"
          type="password"
          class="pa-board-input"
          autocomplete="new-password"
          maxlength="128"
          required
        />
        <span class="pa-board-hint">{{ t('boardPasswordHint') }}</span>
      </label>
      <label class="pa-board-field">
        <span class="pa-board-label">{{ t('boardPasswordConfirmLabel') }}</span>
        <input
          v-model="passwordConfirm"
          type="password"
          class="pa-board-input"
          autocomplete="new-password"
          maxlength="128"
          required
        />
      </label>
      <p class="pa-board-notice pa-board-notice--info">{{ t('boardAfterSubmitPasswordNote') }}</p>
      <p v-if="submitError" class="pa-board-error">{{ submitError }}</p>
      <div class="pa-board-actions">
        <button type="submit" class="primary-btn" :disabled="submitting">
          {{ submitting ? t('boardSubmitting') : t('boardSubmit') }}
        </button>
        <router-link to="/board" class="secondary-btn pa-board-cancel">{{ t('boardBackToList') }}</router-link>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { createBoardPost } from '../api'
import type { ApiResponse } from '../api'
import { useI18n } from '../i18n'

const { t } = useI18n()
const router = useRouter()
const title = ref('')
const body = ref('')
const authorDisplay = ref('')
const password = ref('')
const passwordConfirm = ref('')
const submitting = ref(false)
const submitError = ref<string | null>(null)

async function onSubmit() {
  submitError.value = null
  if (!title.value) {
    submitError.value = t('boardValidationTitle')
    return
  }
  if (!body.value) {
    submitError.value = t('boardValidationBody')
    return
  }
  if (!password.value.trim()) {
    submitError.value = t('boardValidationPassword')
    return
  }
  if (password.value.trim().length < 8) {
    submitError.value = t('boardValidationPasswordLength')
    return
  }
  if (password.value !== passwordConfirm.value) {
    submitError.value = t('boardValidationPasswordMismatch')
    return
  }
  submitting.value = true
  try {
    const detail = await createBoardPost({
      title: title.value,
      body: body.value,
      password: password.value.trim(),
      authorDisplay: authorDisplay.value || null,
    })
    try {
      sessionStorage.setItem(`psych-board-init:${detail.id}`, JSON.stringify(detail))
    } catch {
      /* ignore quota / private mode */
    }
    await router.push(`/board/${detail.id}`)
  } catch (e) {
    if (axios.isAxiosError(e)) {
      const data = e.response?.data as ApiResponse<unknown> | undefined
      submitError.value = data?.message || t('boardCreateError')
    } else {
      submitError.value = t('boardCreateError')
    }
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.pa-board-new {
  max-width: 640px;
  margin: 0 auto;
  padding: 24px 16px 48px;
}

.pa-board-new-title {
  font-family: var(--pa-font-heading);
  font-size: clamp(1.25rem, 3vw, 1.5rem);
  margin: 0 0 20px;
  color: var(--pa-text-strong);
}

.pa-board-form {
  padding: 20px;
}

.pa-board-notice {
  font-size: 14px;
  line-height: 1.5;
  margin: 0 0 16px;
  padding: 12px 14px;
  border-radius: var(--pa-radius-sm);
}

.pa-board-notice--warn {
  background: rgba(180, 83, 9, 0.12);
  color: var(--pa-text-strong);
  border: 1px solid rgba(180, 83, 9, 0.35);
}

.pa-board-notice--info {
  background: var(--pa-bg-section);
  color: var(--pa-text-body);
  border: 1px solid var(--pa-border);
}

.pa-board-field {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-bottom: 16px;
}

.pa-board-label {
  font-weight: 600;
  font-size: 14px;
  color: var(--pa-text-strong);
}

.pa-board-hint {
  font-size: 12px;
  color: var(--pa-text-muted);
}

.pa-board-input,
.pa-board-textarea {
  font-family: var(--pa-font-body);
  font-size: 15px;
  padding: 10px 12px;
  border: 1px solid var(--pa-border);
  border-radius: var(--pa-radius-sm);
  background: var(--pa-bg-elevated);
  color: var(--pa-text-body);
}

.pa-board-textarea {
  resize: vertical;
  min-height: 160px;
}

.pa-board-error {
  color: #b45309;
  font-size: 14px;
  margin: 0 0 12px;
}

.pa-board-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  align-items: center;
}

.pa-board-cancel {
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 44px;
  padding: 0 16px;
}
</style>
