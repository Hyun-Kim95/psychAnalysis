<template>
  <section class="result">
    <div v-if="loading" class="card">
      <p>결과를 불러오는 중입니다...</p>
    </div>

    <div v-else-if="error" class="card card-error">
      <p>{{ error }}</p>
    </div>

    <div v-else-if="data" class="card">
      <h2 class="result-title">{{ data.assessmentName ? `${data.assessmentName} 결과 요약` : '검사 결과 요약' }}</h2>

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

      <div class="t-score-explanation">
        <h4 class="t-score-explanation-title">T점수란?</h4>
        <p>T점수는 원점수를 표준화한 점수로, <strong>평균 50, 표준편차 10</strong>을 기준으로 합니다. 규준(참조 집단)과 비교해 상대적인 위치를 나타냅니다.</p>
        <p>50에 가까울수록 규준 집단의 평균 수준이며, <strong>60 이상</strong>이면 상대적으로 높은 편, <strong>40 미만</strong>이면 상대적으로 낮은 편으로 해석할 수 있습니다. 척도마다 원점수 단위가 달라도 T점수로는 서로 비교가 가능합니다.</p>
      </div>

      <h3 class="result-subtitle">척도별 점수 (엑셀 규준 기준)</h3>
      <table class="scale-table">
        <thead>
          <tr>
            <th>척도</th>
            <th>원점수</th>
            <th>T 점수</th>
          </tr>
        </thead>
        <tbody>
          <template v-if="scaleGroups.length">
            <template v-for="group in scaleGroups" :key="group.groupLabel">
              <tr class="scale-group-header">
                <td colspan="3">{{ group.groupLabel }}</td>
              </tr>
              <tr v-for="code in group.scaleCodes" :key="code" v-show="hasScaleData(code)">
                <td>{{ scaleLabel(code) }}</td>
                <td>{{ (data.scaleRawScores[code] ?? 0).toFixed(1) }}</td>
                <td>{{ (data.scaleTScores[code] ?? 0).toFixed(1) }}</td>
              </tr>
            </template>
          </template>
          <template v-else>
            <tr v-for="code in scaleOrder" :key="code" v-show="hasScaleData(code)">
              <td>{{ scaleLabel(code) }}</td>
              <td>{{ (data.scaleRawScores[code] ?? 0).toFixed(1) }}</td>
              <td>{{ (data.scaleTScores[code] ?? 0).toFixed(1) }}</td>
            </tr>
          </template>
        </tbody>
      </table>

      <h3 class="result-subtitle">척도별 해석</h3>
      <div class="interpretations">
        <template v-if="scaleGroups.length">
          <template v-for="group in scaleGroups" :key="'g-' + group.groupLabel">
            <h4 class="interpretation-group-title">{{ group.groupLabel }}</h4>
            <div
              v-for="code in group.scaleCodes"
              :key="'interp-' + code"
              v-show="data.scaleInterpretations?.[code]"
              class="interpretation-block"
            >
              <h4 class="interpretation-scale">{{ scaleLabel(code) }} · T {{ (data.scaleTScores[code] ?? 0).toFixed(1) }}</h4>
              <p class="interpretation-text">{{ data.scaleInterpretations[code] }}</p>
            </div>
          </template>
        </template>
        <template v-else>
          <div
            v-for="code in scaleOrder"
            :key="'interp-' + code"
            v-show="data.scaleInterpretations?.[code]"
            class="interpretation-block"
          >
            <h4 class="interpretation-scale">{{ scaleLabel(code) }} · T {{ (data.scaleTScores[code] ?? 0).toFixed(1) }}</h4>
            <p class="interpretation-text">{{ data.scaleInterpretations[code] }}</p>
          </div>
        </template>
        <p v-if="hasAnyInterpretation === false" class="interpretation-generic">
          위 척도별 점수와 T점수를 참고해 주세요. 심리상담·임상적 판단이 필요할 경우 전문가와 상담하시기 바랍니다.
        </p>
      </div>

      <p class="result-note">
        위 해석은 검사별 기준(총점 또는 T점수 구간)에 따른 참고 설명입니다. 심리상담·임상적 판단이 필요할 경우 전문가와 상담하시기 바랍니다.
      </p>

      <div class="result-actions">
        <button
          v-if="props.resultId"
          type="button"
          class="primary-btn"
          @click="downloadPdf"
        >
          PDF 다운로드
        </button>
        <button type="button" class="secondary-btn" @click="goMain">
          메인으로
        </button>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { fetchResult, getApiBaseUrl, TCI_SCALE_ORDER, type ResultViewData } from '../api'

const router = useRouter()

const props = defineProps<{
  resultId: string | null
}>()

const loading = ref(true)
const error = ref<string | null>(null)
const data = ref<ResultViewData | null>(null)

const scaleOrder = computed(() => data.value?.scaleOrder?.length ? data.value.scaleOrder! : [...TCI_SCALE_ORDER])
const scaleGroups = computed(() => data.value?.scaleGroups?.length ? data.value.scaleGroups : [])
const hasAnyInterpretation = computed(() => {
  const interp = data.value?.scaleInterpretations
  if (!interp) return false
  return Object.keys(interp).some((code) => interp[code])
})
function scaleLabel(code: string) {
  const names = data.value?.scaleDisplayNames
  return (names && names[code]) ? `${names[code]} (${code})` : code
}
function hasScaleData(code: string) {
  return data.value != null && (
    data.value.scaleRawScores[code] != null || data.value.scaleTScores[code] != null
  )
}

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
  const base = getApiBaseUrl()
  window.open(`${base}/api/results/${props.resultId}/pdf`, '_blank')
}

function goMain() {
  router.push('/')
}
</script>

