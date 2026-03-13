<template>
  <section class="result">
    <div v-if="loading" class="card">
      <p>결과를 불러오는 중입니다...</p>
    </div>

    <div v-else-if="error" class="card card-error">
      <p>{{ error }}</p>
    </div>

    <div v-else-if="data" class="card">
      <h2 class="result-title">검사 결과 요약</h2>

      <div class="result-summary">
        <div class="summary-item">
          <span class="summary-label">총점</span>
          <span class="summary-value">
            {{ data.totalRawScore ?? '-' }}
          </span>
        </div>
        <div class="summary-item">
          <span class="summary-label">총 T점수</span>
          <span class="summary-value">
            {{ data.totalTScore ? data.totalTScore.toFixed(1) : '-' }}
          </span>
        </div>
      </div>

      <h3 class="result-subtitle">하위척도별 점수</h3>
      <table class="scale-table">
        <thead>
          <tr>
            <th>척도</th>
            <th>Raw 점수</th>
            <th>T 점수</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(raw, code) in data.scaleRawScores" :key="code">
            <td>{{ code }}</td>
            <td>{{ raw.toFixed(1) }}</td>
            <td>{{ (data.scaleTScores[code] ?? 0).toFixed(1) }}</td>
          </tr>
        </tbody>
      </table>

      <p class="result-note">
        본 리포트는 프로토타입으로, 실제 임상적 해석이 아닌 연구/테스트 목적의 예시입니다.
      </p>

      <button
        v-if="props.resultId"
        type="button"
        class="primary-btn"
        style="margin-top: 8px;"
        @click="downloadPdf"
      >
        PDF 다운로드
      </button>
    </div>
  </section>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { fetchResult, type ResultViewData } from '../api'

const props = defineProps<{
  resultId: string | null
}>()

const loading = ref(true)
const error = ref<string | null>(null)
const data = ref<ResultViewData | null>(null)

onMounted(async () => {
  if (!props.resultId) {
    error.value = '결과 ID가 없습니다.'
    loading.value = false
    return
  }
  try {
    data.value = await fetchResult(props.resultId)
  } catch (e) {
    error.value = '결과를 불러오지 못했습니다. 잠시 후 다시 시도해 주세요.'
  } finally {
    loading.value = false
  }
})

function downloadPdf() {
  if (!props.resultId) return
  window.open(`/api/results/${props.resultId}/pdf`, '_blank')
}
</script>

