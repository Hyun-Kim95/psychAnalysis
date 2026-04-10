<template>
  <section class="test pa-test pa-test-page" v-if="items.length">
    <div class="pa-test-inner">
      <h1 class="visually-hidden">{{ assessmentName }} — {{ t('testInProgress') }}</h1>
      <header class="pa-test-header">
        <div class="pa-test-toprow">
          <div class="pa-test-heading">
            <p class="pa-test-eyebrow">{{ assessmentEyebrow }}</p>
            <p class="pa-test-progress-line">{{ t('testProgress', { total: items.length, current: currentIndex + 1 }) }}</p>
          </div>
          <p class="pa-test-counter" aria-live="polite">
            {{ currentIndex + 1 }} / {{ items.length }}
          </p>
        </div>
        <div
          class="progress pa-test-progress"
          role="progressbar"
          :aria-valuenow="currentIndex + 1"
          :aria-valuemin="1"
          :aria-valuemax="items.length"
          :aria-label="t('testProgress', { total: items.length, current: currentIndex + 1 })"
        >
          <div class="progress-bar" :style="{ width: progressPercent + '%' }"></div>
        </div>
      </header>

      <div v-if="currentItem" class="pa-question-card">
        <p class="item-number pa-question-label">{{ t('testItemNumber', { index: currentIndex + 1 }) }}</p>
        <p v-if="isBaiAssessment" class="pa-bai-respond-prompt">{{ t('testBaiRespondPrompt') }}</p>
        <h2 class="item-text pa-question-text">{{ currentItem.text }}</h2>

        <div
          class="choices pa-choices"
          :class="choiceLayout === 'tiles' ? 'pa-choices--tiles' : 'pa-choices--stack'"
        >
          <button
            v-for="choice in currentItem.choices"
            :key="choice.value"
            type="button"
            class="choice-btn"
            :class="{ selected: answers[currentItem.id] === choice.value }"
            @click="selectChoice(currentItem.id, choice.value)"
          >
            {{ choice.label }}
          </button>
        </div>

        <p v-if="showValidationError" class="validation-error">
          {{ t('testAnswerRequired') }}
        </p>
      </div>

      <footer class="test-footer pa-test-footer">
        <button
          v-if="currentIndex === 0"
          type="button"
          class="secondary-btn pa-test-prev-btn"
          @click="goToIntro"
        >
          {{ t('testFirst') }}
        </button>
        <button
          v-else
          type="button"
          class="secondary-btn pa-test-prev-btn"
          @click="prevItem"
        >
          {{ t('testPrev') }}
        </button>

        <div class="pa-test-footer-primary">
          <button
            v-if="!isLast"
            type="button"
            class="primary-btn pa-test-next-btn"
            @click="nextItem"
          >
            {{ t('testNext') }}
          </button>
          <button
            v-else
            type="button"
            class="primary-btn pa-test-next-btn"
            :disabled="submitting"
            @click="submit"
          >
            {{ submitting ? t('testSubmitting') : t('testSubmit') }}
          </button>
        </div>
      </footer>

      <p class="pa-test-hint">{{ t('testComfortNote') }}</p>
    </div>
  </section>

  <section v-else class="test pa-test pa-test-page">
    <div class="pa-test-inner">
      <div class="card pa-test-loading">
        <p>{{ t('loadingItems') }}</p>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { fetchAssessment, fetchAssessmentItems, submitAnswers, type AssessmentItem } from '../api'
import { locale, useI18n } from '../i18n'

const props = defineProps<{
  assessmentId: number | null
  sessionId: string | null
}>()

const emit = defineEmits<{
  (e: 'completed', payload: { resultId: string }): void
  (e: 'cancel'): void
}>()

const items = ref<AssessmentItem[]>([])
const assessmentName = ref<string>('')
const currentIndex = ref(0)
const answers = ref<Record<number, number>>({})
const submitting = ref(false)
const showValidationError = ref(false)
const { t } = useI18n()

const currentItem = computed(() => items.value[currentIndex.value])

/** Figma: 2–6개 짧은 라벨은 타일 그리드, 긴 문장형은 세로 스택 */
const choiceLayout = computed<'tiles' | 'stack'>(() => {
  const item = currentItem.value
  if (!item?.choices?.length) return 'stack'
  const labels = item.choices.map((c) => c.label)
  if (item.choices.length < 2 || item.choices.length > 6) return 'stack'
  const maxLen = Math.max(...labels.map((s) => s.length))
  if (maxLen > 36) return 'stack'
  return 'tiles'
})
const isLast = computed(() => currentIndex.value === items.value.length - 1)
const progressPercent = computed(() => {
  if (!items.value.length) return 0
  return Math.round(((currentIndex.value + 1) / items.value.length) * 100)
})

const assessmentEyebrow = computed(() =>
  (assessmentName.value || t('testFallbackTitle')).toUpperCase(),
)

/** BAI는 공식 문항이 짧은 증상 나열이라, 응답 방식(기간·선택지 의미)을 화면에서 한 번 더 안내 */
const isBaiAssessment = computed(() => /bai/i.test(assessmentName.value || ''))

async function loadAssessmentAndItems() {
  if (!props.assessmentId) return
  const [assessment, itemsData] = await Promise.all([
    fetchAssessment(props.assessmentId),
    fetchAssessmentItems(props.assessmentId),
  ])
  assessmentName.value = assessment.name
  items.value = itemsData.items
  if (currentIndex.value >= items.value.length) {
    currentIndex.value = Math.max(0, items.value.length - 1)
  }
}

onMounted(loadAssessmentAndItems)
watch(locale, loadAssessmentAndItems)
watch(() => props.assessmentId, loadAssessmentAndItems)

function selectChoice(itemId: number, value: number) {
  answers.value[itemId] = value
  showValidationError.value = false
  if (!isLast.value) {
    currentIndex.value += 1
  }
}

function ensureAnswered(): boolean {
  const itemId = currentItem.value.id
  if (!(itemId in answers.value)) {
    showValidationError.value = true
    return false
  }
  return true
}

function nextItem() {
  if (!ensureAnswered()) return
  if (!isLast.value) {
    currentIndex.value += 1
  }
}

function prevItem() {
  if (currentIndex.value > 0) {
    currentIndex.value -= 1
    showValidationError.value = false
  }
}

function goToIntro() {
  emit('cancel')
}

async function submit() {
  if (!ensureAnswered()) return
  if (!props.sessionId) return

  submitting.value = true
  try {
    const payload = Object.entries(answers.value).map(([itemId, value]) => ({
      itemId: Number(itemId),
      value: value as number,
    }))
    const res = await submitAnswers(props.sessionId, payload)
    emit('completed', { resultId: res.resultId })
  } finally {
    submitting.value = false
  }
}
</script>
