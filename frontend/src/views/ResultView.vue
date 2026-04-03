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

      <template v-if="orderedScaleCodes.length">
        <h3 class="result-subtitle">척도별 T점수 그래프</h3>
        <p class="result-chart-hint">
          막대 색: T점수 기준 — 파란색(40 미만), 회색(40~60), 주황색(60 초과). 세로 점선은 규준 평균 <strong>50</strong>입니다. 원점수는 아래 표에서 확인할 수 있습니다.
        </p>
        <div class="result-charts result-charts--single">
          <div class="result-chart-wrap">
            <div class="result-chart-canvas" :style="{ height: chartHeightPx + 'px' }">
              <canvas ref="chartTScoreCanvas" />
            </div>
          </div>
        </div>
      </template>

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
              <p class="interpretation-text">{{ data.scaleInterpretations?.[code] }}</p>
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
            <p class="interpretation-text">{{ data.scaleInterpretations?.[code] }}</p>
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
import { Chart } from 'chart.js/auto'
import { computed, nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { fetchResult, getApiBaseUrl, TCI_SCALE_ORDER, type ResultViewData } from '../api'

const router = useRouter()

const props = defineProps<{
  resultId: string | null
}>()

const loading = ref(true)
const error = ref<string | null>(null)
const data = ref<ResultViewData | null>(null)

const chartTScoreCanvas = ref<HTMLCanvasElement | null>(null)
let chartInstances: Chart[] = []

const scaleOrder = computed(() => data.value?.scaleOrder?.length ? data.value.scaleOrder! : [...TCI_SCALE_ORDER])
const scaleGroups = computed(() => data.value?.scaleGroups?.length ? data.value.scaleGroups : [])
const hasAnyInterpretation = computed(() => {
  const interp = data.value?.scaleInterpretations
  if (!interp) return false
  return Object.keys(interp).some((code) => interp[code])
})

/** 표·그래프에 쓰는 척도 순서 (데이터가 있는 척도만) */
const orderedScaleCodes = computed(() => {
  const d = data.value
  if (!d) return []
  const groups = scaleGroups.value
  if (groups.length) {
    const out: string[] = []
    for (const g of groups) {
      for (const code of g.scaleCodes) {
        if (hasScaleData(code)) out.push(code)
      }
    }
    return out
  }
  return scaleOrder.value.filter((code) => hasScaleData(code))
})

const chartHeightPx = computed(() => {
  const n = orderedScaleCodes.value.length
  if (n <= 0) return 200
  // T점수 차트만 표시 — NEO 등 척도 수가 많을 때 잘림 방지
  return Math.min(900, Math.max(200, 40 + n * 28))
})

function scaleLabel(code: string) {
  const names = data.value?.scaleDisplayNames
  return (names && names[code]) ? `${names[code]} (${code})` : code
}

/** 그래프 Y축용 짧은 라벨 */
function chartAxisLabel(code: string) {
  const names = data.value?.scaleDisplayNames
  if (names?.[code]) return names[code]
  return code
}

function hasScaleData(code: string) {
  return data.value != null && (
    data.value.scaleRawScores[code] != null || data.value.scaleTScores[code] != null
  )
}

function tScoreBarColor(t: number) {
  if (t < 40) return 'rgba(59, 130, 246, 0.82)'
  if (t > 60) return 'rgba(245, 158, 11, 0.82)'
  return 'rgba(107, 114, 128, 0.72)'
}

function destroyResultCharts() {
  chartInstances.forEach((c) => c.destroy())
  chartInstances = []
}

function buildResultCharts() {
  destroyResultCharts()
  const d = data.value
  if (!d || !orderedScaleCodes.value.length) return

  const codes = orderedScaleCodes.value
  const labels = codes.map((c) => chartAxisLabel(c))
  const tValues = codes.map((c) => d.scaleTScores[c] ?? 0)
  const tColors = tValues.map((t) => tScoreBarColor(t))

  const commonOptions = {
    responsive: true,
    maintainAspectRatio: false,
    indexAxis: 'y' as const,
    plugins: {
      legend: { display: false },
      tooltip: {
        callbacks: {
          title: (items: { label: string; dataIndex: number }[]) => {
            const i = items[0]?.dataIndex ?? 0
            return scaleLabel(codes[i])
          },
        },
      },
    },
    scales: {
      x: { beginAtZero: true, grid: { color: 'rgba(0,0,0,0.06)' } },
      y: { grid: { display: false }, ticks: { font: { size: 12 } } },
    },
  }

  if (chartTScoreCanvas.value) {
    const tMin = Math.min(...tValues, 50)
    const tMax = Math.max(...tValues, 50)
    const pad = 6
    const chart = new Chart(chartTScoreCanvas.value, {
      type: 'bar',
      data: {
        labels,
        datasets: [
          {
            label: 'T 점수',
            data: tValues,
            backgroundColor: tColors,
            borderWidth: 0,
          },
        ],
      },
      options: {
        ...commonOptions,
        scales: {
          ...commonOptions.scales,
          x: {
            ...commonOptions.scales.x,
            min: Math.max(0, Math.floor((tMin - pad) / 5) * 5),
            max: Math.ceil((tMax + pad) / 5) * 5,
            grid: {
              color: (ctx) => {
                if (ctx.tick.value === 50) return 'rgba(99, 102, 241, 0.5)'
                return 'rgba(0,0,0,0.06)'
              },
              lineWidth: (ctx) => (ctx.tick.value === 50 ? 2 : 1),
            },
            ticks: {
              stepSize: 5,
              callback(value: string | number) {
                const n = typeof value === 'number' ? value : Number(value)
                return Number.isFinite(n) ? n : value
              },
            },
          },
        },
      },
    })
    chartInstances.push(chart)
    chart.resize()
  }
}

async function refreshCharts() {
  await nextTick()
  setTimeout(() => buildResultCharts(), 80)
}

watch(
  () => [data.value, loading.value, orderedScaleCodes.value.join(',')] as const,
  () => {
    if (loading.value || !data.value || !orderedScaleCodes.value.length) {
      destroyResultCharts()
      return
    }
    void refreshCharts()
  },
)

onBeforeUnmount(destroyResultCharts)

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

