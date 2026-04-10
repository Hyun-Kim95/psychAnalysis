<template>
  <section class="intro pa-intro">
    <div v-if="loading" class="card">
      <p>{{ t('loadingAssessmentInfo') }}</p>
    </div>

    <div v-else-if="error" class="card card-error">
      <p>{{ error }}</p>
    </div>

    <div v-else-if="assessment" class="card">
      <h2 class="intro-title">{{ assessment.name }}</h2>
      <p class="intro-desc">
        {{ assessment.description || t('defaultAssessmentDesc') }}
      </p>

      <template v-if="ready">
        <ul class="intro-list">
          <li>{{ t('introInfo1', { count: itemCount, minutes: estimatedMinutes }) }}</li>
          <li>{{ t('introInfo2') }}</li>
          <li>{{ t('introInfo3') }}</li>
        </ul>

        <div class="intro-actions">
          <button class="primary-btn" :disabled="starting" @click="start">
            {{ starting ? t('introSessionStarting') : t('introStart') }}
          </button>
          <button type="button" class="secondary-btn" @click="emit('back')">
            {{ t('listButton') }}
          </button>
        </div>
      </template>
      <template v-else>
        <p class="intro-preparing">{{ t('introPreparing') }}</p>
        <button type="button" class="secondary-btn" @click="emit('back')">
          {{ t('listButton') }}
        </button>
      </template>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { createSession, fetchAssessment, fetchAssessmentItems, type AssessmentSummary } from '../api'
import { locale, useI18n } from '../i18n'

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
const { t } = useI18n()

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
    error.value = t('introLoadError')
  } finally {
    loading.value = false
  }
}

onMounted(load)
watch(() => props.assessmentId, load)
watch(locale, load)

async function start() {
  if (!assessment.value || !ready.value) return
  starting.value = true
  try {
    const session = await createSession(assessment.value.id)
    emit('start', { assessmentId: assessment.value.id, sessionId: session.responseSessionId })
  } catch (e) {
    error.value = t('introCreateSessionError')
  } finally {
    starting.value = false
  }
}
</script>
