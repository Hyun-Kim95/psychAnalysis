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
        {{ assessment.description || '온라인 심리 검사입니다.' }}
      </p>

      <template v-if="ready">
        <ul class="intro-list">
          <li>문항 수: {{ itemCount }}문항 (예상 소요 시간: 약 {{ estimatedMinutes }}분)</li>
          <li>응답은 익명으로 저장되며, 결과 해석은 참고용으로 활용해 주세요.</li>
          <li>검사 도중 창을 닫으면 응답이 저장되지 않아 결과를 볼 수 없습니다. 여유 있는 시간에 진행해 주세요.</li>
        </ul>

        <div class="intro-actions">
          <button class="primary-btn" :disabled="starting" @click="start">
            {{ starting ? '검사 세션 생성 중...' : '검사 시작하기' }}
          </button>
          <button type="button" class="secondary-btn" @click="emit('back')">
            목록으로
          </button>
        </div>
      </template>
      <template v-else>
        <p class="intro-preparing">이 검사는 현재 준비 중입니다. 추후 제공될 예정입니다.</p>
        <button type="button" class="secondary-btn" @click="emit('back')">
          목록으로
        </button>
      </template>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { createSession, fetchAssessment, fetchAssessmentItems, type AssessmentSummary } from '../api'

const props = defineProps<{
  assessmentId: number
}>()

const emit = defineEmits<{
  (e: 'start', payload: { assessmentId: number; sessionId: string }): void
  (e: 'back'): void
}>()

const assessment = ref<AssessmentSummary | null>(null)
const itemCount = ref(0)
const loading = ref(true)
const error = ref<string | null>(null)
const starting = ref(false)

const ready = computed(() => itemCount.value > 0)
const estimatedMinutes = computed(() => Math.max(1, Math.ceil(itemCount.value / 5)))

async function load() {
  if (!props.assessmentId) return
  loading.value = true
  error.value = null
  try {
    assessment.value = await fetchAssessment(props.assessmentId)
    const itemsData = await fetchAssessmentItems(props.assessmentId)
    itemCount.value = itemsData.items?.length ?? 0
  } catch (e) {
    error.value = '검사 정보를 불러오지 못했습니다. 잠시 후 다시 시도해 주세요.'
  } finally {
    loading.value = false
  }
}

onMounted(load)
watch(() => props.assessmentId, load)

async function start() {
  if (!assessment.value || !ready.value) return
  starting.value = true
  try {
    const session = await createSession(assessment.value.id)
    emit('start', { assessmentId: assessment.value.id, sessionId: session.responseSessionId })
  } catch (e) {
    error.value = '검사 세션을 생성하지 못했습니다. 잠시 후 다시 시도해 주세요.'
  } finally {
    starting.value = false
  }
}
</script>
