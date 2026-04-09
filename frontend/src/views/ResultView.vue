<template>
  <section class="result">
    <div v-if="loading" class="card">
      <p>{{ t('loadingResult') }}</p>
    </div>

    <div v-else-if="error" class="card card-error">
      <p>{{ error }}</p>
    </div>

    <div v-else-if="data" class="card">
      <h2 class="result-title">{{ data.assessmentName ? t('resultSummaryTitleWithName', { name: data.assessmentName }) : t('resultSummaryTitleDefault') }}</h2>

      <div class="result-summary">
        <div class="summary-item">
          <span class="summary-label">{{ t('totalRawScore') }}</span>
          <span class="summary-value">
            {{ data.totalRawScore ?? '-' }}
          </span>
        </div>
        <div class="summary-item">
          <span class="summary-label">{{ t('totalTScore') }}</span>
          <span class="summary-value">
            {{ data.totalTScore ? data.totalTScore.toFixed(1) : '-' }}
          </span>
        </div>
      </div>

      <div class="t-score-explanation">
        <h4 class="t-score-explanation-title">{{ t('tScoreTitle') }}</h4>
        <p>{{ t('tScoreDesc1') }}</p>
        <p>{{ t('tScoreDesc2') }}</p>
      </div>

      <template v-if="orderedScaleCodes.length">
        <h3 class="result-subtitle">{{ t('resultChartTitle') }}</h3>
        <p class="result-chart-hint">
          {{ t('resultChartHint') }}
        </p>
        <div class="result-charts result-charts--single">
          <div class="result-chart-wrap">
            <div class="result-chart-canvas" :style="{ height: chartHeightPx + 'px' }">
              <canvas ref="chartTScoreCanvas" />
            </div>
          </div>
        </div>
      </template>

      <h3 class="result-subtitle">{{ t('resultScaleScores') }}</h3>
      <table class="scale-table">
        <thead>
          <tr>
            <th>{{ t('scale') }}</th>
            <th>{{ t('rawScore') }}</th>
            <th>{{ t('tScore') }}</th>
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

      <h3 class="result-subtitle">{{ t('resultInterpretation') }}</h3>
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
          {{ t('resultNoInterpretation') }}
        </p>
      </div>

      <p class="result-note">
        {{ t('resultNote') }}
      </p>

      <div class="result-actions">
        <button
          v-if="props.resultId"
          type="button"
          class="primary-btn"
          @click="downloadPdf"
        >
          {{ t('downloadPdf') }}
        </button>
        <button type="button" class="secondary-btn" @click="goMain">
          {{ t('goMain') }}
        </button>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { Chart } from 'chart.js/auto'
import { computed, nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { downloadUserResultPdf, fetchResult, TCI_SCALE_ORDER, type ResultViewData } from '../api'
import { useI18n } from '../i18n'

const router = useRouter()

const props = defineProps<{
  resultId: string | null
}>()

const loading = ref(true)
const error = ref<string | null>(null)
const data = ref<ResultViewData | null>(null)
const { t } = useI18n()

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
            label: t('tScore'),
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
    error.value = t('resultIdMissing')
    loading.value = false
    return
  }
  try {
    data.value = await fetchResult(props.resultId)
  } catch (e) {
    error.value = t('resultLoadError')
  } finally {
    loading.value = false
  }
})

async function downloadPdf() {
  if (!props.resultId) return
  try {
    await downloadUserResultPdf(props.resultId)
  } catch {
    // 실패 시 무시 (필요 시 토스트 추가)
  }
}

function goMain() {
  router.push('/')
}
</script>

