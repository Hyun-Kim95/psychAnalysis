<template>
  <section class="admin-login">
    <div class="card">
      <h2 class="intro-title">{{ t('adminLoginTitle') }}</h2>
      <p class="intro-desc">{{ t('adminLoginDesc') }}</p>

      <form class="admin-form" @submit.prevent="submit">
        <label class="admin-field">
          <span>{{ t('adminId') }}</span>
          <input v-model="loginId" type="text" autocomplete="username" required />
        </label>

        <label class="admin-field">
          <span>{{ t('adminPassword') }}</span>
          <input v-model="password" type="password" autocomplete="current-password" required />
        </label>

        <p v-if="error" class="validation-error">{{ error }}</p>

        <button class="primary-btn" type="submit" :disabled="loading">
          {{ loading ? t('adminLoginLoading') : t('adminLogin') }}
        </button>
      </form>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { adminLogin } from '../api'
import { useI18n } from '../i18n'

const emit = defineEmits<{
  (e: 'logged-in', payload: { token: string }): void
}>()

const loginId = ref('')
const password = ref('')
const loading = ref(false)
const error = ref<string | null>(null)
const { t } = useI18n()

async function submit() {
  loading.value = true
  error.value = null
  try {
    const res = await adminLogin(loginId.value, password.value)
    emit('logged-in', { token: res.token })
  } catch (e) {
    error.value = t('adminLoginError')
  } finally {
    loading.value = false
  }
}
</script>

