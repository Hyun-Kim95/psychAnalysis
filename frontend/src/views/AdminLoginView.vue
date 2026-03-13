<template>
  <section class="admin-login">
    <div class="card">
      <h2 class="intro-title">관리자 로그인</h2>
      <p class="intro-desc">접속 통계와 응답 현황을 조회하려면 로그인하세요.</p>

      <form class="admin-form" @submit.prevent="submit">
        <label class="admin-field">
          <span>아이디</span>
          <input v-model="loginId" type="text" autocomplete="username" required />
        </label>

        <label class="admin-field">
          <span>비밀번호</span>
          <input v-model="password" type="password" autocomplete="current-password" required />
        </label>

        <p v-if="error" class="validation-error">{{ error }}</p>

        <button class="primary-btn" type="submit" :disabled="loading">
          {{ loading ? '로그인 중...' : '로그인' }}
        </button>
      </form>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { adminLogin } from '../api'

const emit = defineEmits<{
  (e: 'logged-in', payload: { token: string }): void
}>()

const loginId = ref('')
const password = ref('')
const loading = ref(false)
const error = ref<string | null>(null)

async function submit() {
  loading.value = true
  error.value = null
  try {
    const res = await adminLogin(loginId.value, password.value)
    emit('logged-in', { token: res.token })
  } catch (e) {
    error.value = '로그인에 실패했습니다. 아이디/비밀번호를 확인해 주세요.'
  } finally {
    loading.value = false
  }
}
</script>

