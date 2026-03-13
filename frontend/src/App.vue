<template>
  <div id="app-root">
    <header class="app-header">
      <h1 class="app-title">심리검사 프로토타입</h1>
      <p class="app-subtitle">온라인 심리검사 체험 서비스</p>
    </header>

    <nav class="app-nav">
      <button
        class="nav-btn"
        :class="{ active: mode === 'user' }"
        type="button"
        @click="mode = 'user'"
      >
        사용자 모드
      </button>
      <button
        class="nav-btn"
        :class="{ active: mode === 'admin' }"
        type="button"
        @click="mode = 'admin'"
      >
        관리자 모드
      </button>
    </nav>

    <main class="app-main">
      <template v-if="mode === 'user'">
        <IntroView v-if="step === 'intro'" @start="handleStart" />
        <TestView
          v-else-if="step === 'test'"
          :assessment-id="assessmentId"
          :session-id="sessionId"
          @completed="handleCompleted"
        />
        <ResultView
          v-else-if="step === 'result'"
          :result-id="resultId"
        />
      </template>
      <template v-else>
        <AdminLoginView v-if="!adminToken" @logged-in="handleAdminLoggedIn" />
        <AdminDashboardView v-else :token="adminToken" />
      </template>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import IntroView from './views/IntroView.vue'
import TestView from './views/TestView.vue'
import ResultView from './views/ResultView.vue'
import AdminLoginView from './views/AdminLoginView.vue'
import AdminDashboardView from './views/AdminDashboardView.vue'

type Step = 'intro' | 'test' | 'result'
type Mode = 'user' | 'admin'

const mode = ref<Mode>('user')
const step = ref<Step>('intro')
const assessmentId = ref<number | null>(null)
const sessionId = ref<string | null>(null)
const resultId = ref<string | null>(null)
const adminToken = ref<string | null>(null)

function handleStart(payload: { assessmentId: number; sessionId: string }) {
  assessmentId.value = payload.assessmentId
  sessionId.value = payload.sessionId
  step.value = 'test'
}

function handleCompleted(payload: { resultId: string }) {
  resultId.value = payload.resultId
  step.value = 'result'
}

function handleAdminLoggedIn(payload: { token: string }) {
  adminToken.value = payload.token
}
</script>

