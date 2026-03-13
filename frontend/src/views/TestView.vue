<template>
  <section class="test" v-if="items.length">
    <div class="card">
      <header class="test-header">
        <h2>문항에 응답해 주세요</h2>
        <p class="test-sub">
          총 {{ items.length }}문항 중
          <strong>{{ currentIndex + 1 }}</strong>번 문항
        </p>
        <div class="progress">
          <div class="progress-bar" :style="{ width: progressPercent + '%' }"></div>
        </div>
      </header>

      <div class="test-body">
        <p class="item-number">문항 {{ currentItem.itemNumber }}</p>
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
          현재 문항에 대한 응답을 선택해 주세요.
        </p>
      </div>

      <footer class="test-footer">
        <button type="button" class="secondary-btn" :disabled="currentIndex === 0" @click="prevItem">
          이전
        </button>

        <button
          v-if="!isLast"
          type="button"
          class="primary-btn"
          @click="nextItem"
        >
          다음
        </button>
        <button
          v-else
          type="button"
          class="primary-btn"
          :disabled="submitting"
          @click="submit"
        >
          {{ submitting ? '제출 중...' : '제출하기' }}
        </button>
      </footer>
    </div>
  </section>

  <section v-else class="test">
    <div class="card">
      <p>검사 문항을 불러오는 중입니다...</p>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { fetchCurrentAssessmentItems, submitAnswers, type AssessmentItem } from '../api'

const props = defineProps<{
  assessmentId: number | null
  sessionId: string | null
}>()

const emit = defineEmits<{
  (e: 'completed', payload: { resultId: string }): void
}>()

const items = ref<AssessmentItem[]>([])
const currentIndex = ref(0)
const answers = ref<Record<number, number>>({})
const submitting = ref(false)
const showValidationError = ref(false)

const currentItem = computed(() => items.value[currentIndex.value])
const isLast = computed(() => currentIndex.value === items.value.length - 1)
const progressPercent = computed(() => {
  if (!items.value.length) return 0
  return Math.round(((currentIndex.value + 1) / items.value.length) * 100)
})

onMounted(async () => {
  const data = await fetchCurrentAssessmentItems()
  items.value = data.items
})

function selectChoice(itemId: number, value: number) {
  answers.value[itemId] = value
  showValidationError.value = false
}

function ensureAnswered(): boolean {
  const itemId = currentItem.value.id
  if (!answers.value[itemId]) {
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

