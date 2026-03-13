<template>
  <section class="intro">
    <div v-if="loading" class="card">
      <p>검사 정보를 불러오는 중입니다...</p>
    </div>

    <div v-else-if="error" class="card card-error">
      <p>{{ error }}</p>
    </div>

    <div v-else-if="assessment" class="card">
      <h2 class="intro-title">{{ assessment.name }}</h2>
      <p class="intro-desc">
        {{ assessment.description || '불안, 회복탄력성, 스트레스를 확인하는 온라인 심리검사입니다.' }}
      </p>

      <ul class="intro-list">
        <li>문항 수: 30문항 (예상 소요 시간: 약 5~10분)</li>
        <li>이 검사는 연구용 프로토타입으로, 익명으로 응답합니다.</li>
        <li>중간에 창을 닫으면 결과를 볼 수 없습니다. 시간을 확보한 뒤 진행해 주세요.</li>
      </ul>

      <button class="primary-btn" :disabled="starting" @click="start">
        {{ starting ? '검사 세션 생성 중...' : '검사 시작하기' }}
      </button>
    </div>
  </section>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { createSession, fetchCurrentAssessment, type AssessmentSummary } from '../api'

const emit = defineEmits<{
  (e: 'start', payload: { assessmentId: number; sessionId: string }): void
}>()

const assessment = ref<AssessmentSummary | null>(null)
const loading = ref(true)
const error = ref<string | null>(null)
const starting = ref(false)

onMounted(async () => {
  try {
    assessment.value = await fetchCurrentAssessment()
  } catch (e) {
    error.value = '검사 정보를 불러오지 못했습니다. 잠시 후 다시 시도해 주세요.'
  } finally {
    loading.value = false
  }
})

async function start() {
  if (!assessment.value) return
  starting.value = true
  try {
    const session = await createSession()
    emit('start', { assessmentId: assessment.value.id, sessionId: session.responseSessionId })
  } catch (e) {
    error.value = '검사 세션을 생성하지 못했습니다. 잠시 후 다시 시도해 주세요.'
  } finally {
    starting.value = false
  }
}
</script>

