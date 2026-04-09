<template>
  <section class="test" v-if="items.length">
    <div class="card">
      <header class="test-header">
        <h2>{{ assessmentName || t('testFallbackTitle') }}</h2>
        <p class="test-sub">
          {{ t('testProgress', { total: items.length, current: currentIndex + 1 }) }}
        </p>
        <div class="progress">
          <div class="progress-bar" :style="{ width: progressPercent + '%' }"></div>
        </div>
      </header>

      <div class="test-body">
        <p class="item-number">{{ t('testItemNumber', { index: currentIndex + 1 }) }}</p>
        <p class="item-text">{{ currentItem.text }}</p>

        <div class="choices">
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

      <footer class="test-footer">
        <button
          v-if="currentIndex === 0"
          type="button"
          class="secondary-btn"
          @click="goToIntro"
        >
          {{ t('testFirst') }}
        </button>
        <button
          v-else
          type="button"
          class="secondary-btn"
          @click="prevItem"
        >
          {{ t('testPrev') }}
        </button>

        <button
          v-if="!isLast"
          type="button"
          class="primary-btn"
          @click="nextItem"
        >
          {{ t('testNext') }}
        </button>
        <button
          v-else
          type="button"
          class="primary-btn"
          :disabled="submitting"
          @click="submit"
        >
          {{ submitting ? t('testSubmitting') : t('testSubmit') }}
        </button>
      </footer>
    </div>
  </section>

  <section v-else class="test">
    <div class="card">
      <p>{{ t('loadingItems') }}</p>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { fetchAssessment, fetchAssessmentItems, submitAnswers, type AssessmentItem } from '../api'
import { useI18n } from '../i18n'

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
const isLast = computed(() => currentIndex.value === items.value.length - 1)
const progressPercent = computed(() => {
  if (!items.value.length) return 0
  return Math.round(((currentIndex.value + 1) / items.value.length) * 100)
})

onMounted(async () => {
  if (!props.assessmentId) return
  const [assessment, itemsData] = await Promise.all([
    fetchAssessment(props.assessmentId),
    fetchAssessmentItems(props.assessmentId),
  ])
  assessmentName.value = assessment.name
  items.value = itemsData.items
})

function selectChoice(itemId: number, value: number) {
  answers.value[itemId] = value
  showValidationError.value = false
  // 보기 선택 시 자동으로 다음 문항으로 이동 (마지막 문항이면 그대로, 이전/다음 버튼으로 수정 가능)
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

