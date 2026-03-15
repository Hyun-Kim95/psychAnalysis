<template>
  <section class="admin-dashboard">
    <div v-if="loading" class="card">
      <p>대시보드 정보를 불러오는 중입니다...</p>
    </div>

    <div v-else-if="error" class="card card-error">
      <p>{{ error }}</p>
    </div>

    <div v-else-if="data" class="card">
      <h2 class="intro-title">관리자 대시보드</h2>
      <p class="intro-desc">서비스 사용 현황 요약입니다.</p>

      <div class="result-actions" style="margin-bottom: 16px; display: flex; flex-wrap: wrap; gap: 8px;">
        <button
          type="button"
          class="primary-btn"
          @click="downloadAdminSummary"
          :disabled="summaryDownloading"
        >
          대시보드 요약 PDF
        </button>
        <button
          type="button"
          class="secondary-btn"
          @click="downloadAdminReference"
          :disabled="referenceDownloading"
        >
          기준·해석 PDF
        </button>
      </div>

      <div class="result-summary">
        <div class="summary-item">
          <span class="summary-label">총 검사 완료 수</span>
          <span class="summary-value">{{ data.totalCompleted }}</span>
        </div>
        <div class="summary-item">
          <span class="summary-label">총 결과 리포트 수</span>
          <span class="summary-value">{{ data.totalResults }}</span>
        </div>
        <div class="summary-item">
          <span class="summary-label">오늘 검사 완료 수</span>
          <span class="summary-value">{{ data.todayCompleted }}</span>
        </div>
      </div>

      <div v-if="Object.keys(reliability).length" class="result-summary" style="margin-top: 8px;">
        <div
          v-for="(alpha, code) in reliability"
          :key="code"
          class="summary-item"
        >
          <span class="summary-label">신뢰도 α ({{ code }})</span>
          <span class="summary-value">{{ alpha.toFixed(2) }}</span>
        </div>
      </div>

      <h3 class="result-subtitle" style="margin-top: 20px;">통계 그래프</h3>
      <div class="charts-grid">
        <div class="chart-wrap">
          <h4 class="chart-title">검사별 응답 수</h4>
          <div class="chart-container">
            <canvas ref="chartAssessmentRef"></canvas>
          </div>
        </div>
        <div class="chart-wrap">
          <h4 class="chart-title">검사별 평균 총점</h4>
          <div class="chart-container">
            <canvas ref="chartAvgScoreRef"></canvas>
          </div>
        </div>
        <div class="chart-wrap">
          <h4 class="chart-title">척도별 신뢰도 (Cronbach α)</h4>
          <div class="chart-container">
            <canvas ref="chartReliabilityRef"></canvas>
          </div>
        </div>
        <div class="chart-wrap">
          <h4 class="chart-title">총 T점수 분포</h4>
          <div class="chart-container">
            <canvas ref="chartTScoreRef"></canvas>
          </div>
        </div>
      </div>

      <h3 class="result-subtitle" style="margin-top: 20px;">검사별 기준점수 및 해석</h3>
      <div class="ref-t-score-explanation">
        <h4 class="ref-subtitle">T점수란?</h4>
        <p>T점수는 원점수를 표준화한 점수로, <strong>평균 50, 표준편차 10</strong>을 기준으로 합니다. 규준(참조 집단)과 비교한 상대적 위치를 나타냅니다.</p>
        <p>원점수가 규준 평균과 같을 때 T점수는 <strong>50</strong>이며, 60 이상이면 상대적으로 높은 편, 40 미만이면 낮은 편으로 해석합니다. 척도별 원점수는 이 규준(평균·표준편차)을 기준으로 T점수로 변환해 해석할 수 있습니다.</p>
        <p class="ref-simple-notice">아래 기준점수 및 해석은 <strong>간단 검사</strong> 기준입니다. (상세 검사도 T점수·해석 기준은 동일합니다.)</p>
      </div>
      <div v-if="referenceLoading" class="ref-loading">기준점수·해석 정보를 불러오는 중...</div>
      <div v-else class="reference-list">
        <details
          v-for="ref in referenceList"
          :key="ref.assessmentName"
          class="reference-block"
        >
          <summary>{{ ref.assessmentName }}</summary>
          <div v-if="ref.norms.length" class="ref-norms">
            <h4 class="ref-subtitle">기준점수(규준)</h4>
            <template v-if="ref.norms.length === 1">
              <p class="ref-norms-single">
                {{ ref.norms[0].scaleName || ref.norms[0].scaleCode }}:
                평균 {{ ref.norms[0].mean != null ? ref.norms[0].mean.toFixed(2) : '-' }},
                표준편차 {{ ref.norms[0].sd != null ? ref.norms[0].sd.toFixed(2) : '-' }}
              </p>
            </template>
            <table v-else class="scale-table">
              <thead>
                <tr>
                  <th>척도</th>
                  <th>평균</th>
                  <th>표준편차</th>
                </tr>
              </thead>
              <tbody>
                <template v-if="ref.scaleGroups?.length">
                  <template v-for="group in ref.scaleGroups" :key="group.groupLabel">
                    <tr class="scale-group-header">
                      <td colspan="3">{{ group.groupLabel }}</td>
                    </tr>
                    <tr v-for="row in refNormsForGroup(ref, group)" :key="row.scaleCode">
                      <td>{{ row.scaleName || row.scaleCode }}</td>
                      <td>{{ row.mean != null ? row.mean.toFixed(2) : '-' }}</td>
                      <td>{{ row.sd != null ? row.sd.toFixed(2) : '-' }}</td>
                    </tr>
                  </template>
                  <tr v-if="refTotalNorm(ref)" :key="'total'">
                    <td>{{ refTotalNorm(ref)!.scaleName || '총점' }}</td>
                    <td>{{ refTotalNorm(ref)!.mean != null ? refTotalNorm(ref)!.mean!.toFixed(2) : '-' }}</td>
                    <td>{{ refTotalNorm(ref)!.sd != null ? refTotalNorm(ref)!.sd!.toFixed(2) : '-' }}</td>
                  </tr>
                </template>
                <template v-else>
                  <tr v-for="row in ref.norms" :key="row.scaleCode">
                    <td>{{ row.scaleName || row.scaleCode }}</td>
                    <td>{{ row.mean != null ? row.mean.toFixed(2) : '-' }}</td>
                    <td>{{ row.sd != null ? row.sd.toFixed(2) : '-' }}</td>
                  </tr>
                </template>
              </tbody>
            </table>
          </div>
          <div v-if="ref.interpretationGuide" class="ref-guide">
            <h4 class="ref-subtitle">해석 기준</h4>
            <pre class="ref-guide-text">{{ ref.interpretationGuide }}</pre>
          </div>
        </details>
        <p v-if="!referenceList.length && !referenceLoading">표시할 검사 기준이 없습니다.</p>
      </div>

      <h3 class="result-subtitle" style="margin-top: 20px;">접속 로그 (IP·날짜·이벤트별 횟수)</h3>
      <table class="scale-table">
        <thead>
          <tr>
            <th>IP</th>
            <th>날짜</th>
            <th>이벤트</th>
            <th>횟수</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="log in pagedLogs" :key="log.id">
            <td>{{ log.clientIpMasked }}</td>
            <td>{{ log.logDate }}</td>
            <td>{{ log.eventType }}</td>
            <td>{{ log.count }}</td>
          </tr>
          <tr v-if="!logs.length">
            <td colspan="4">표시할 로그가 없습니다.</td>
          </tr>
        </tbody>
      </table>
      <div v-if="logsTotalPages > 1" class="pagination">
        <button
          type="button"
          class="pager-btn"
          :disabled="logsPage <= 1"
          @click="logsPage > 1 && (logsPage = logsPage - 1)"
        >
          이전
        </button>
        <span class="pager-info">
          {{ logsPage }} / {{ logsTotalPages }}
        </span>
        <button
          type="button"
          class="pager-btn"
          :disabled="logsPage >= logsTotalPages"
          @click="logsPage < logsTotalPages && (logsPage = logsPage + 1)"
        >
          다음
        </button>
      </div>

      <h3 class="result-subtitle" style="margin-top: 20px;">응답 목록</h3>
      <table class="scale-table">
        <thead>
          <tr>
            <th>검사</th>
            <th>응답 ID</th>
            <th>제출 시각</th>
            <th>총점</th>
            <th>총 T점수</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="r in pagedResponses"
            :key="r.sessionId"
            @click="selectResponse(r)"
            style="cursor: pointer;"
          >
            <td>{{ r.assessmentName ?? '-' }}</td>
            <td>{{ r.sessionId }}</td>
            <td>{{ formatDateTime(r.submittedAt) }}</td>
            <td>{{ r.totalRawScore ?? '-' }}</td>
            <td>{{ r.totalTScore ?? '-' }}</td>
          </tr>
          <tr v-if="!responses.length">
            <td colspan="5">표시할 응답이 없습니다.</td>
          </tr>
        </tbody>
      </table>
      <div v-if="responsesTotalPages > 1" class="pagination">
        <button
          type="button"
          class="pager-btn"
          :disabled="responsesPage <= 1"
          @click="responsesPage > 1 && (responsesPage = responsesPage - 1)"
        >
          이전
        </button>
        <span class="pager-info">
          {{ responsesPage }} / {{ responsesTotalPages }}
        </span>
        <button
          type="button"
          class="pager-btn"
          :disabled="responsesPage >= responsesTotalPages"
          @click="responsesPage < responsesTotalPages && (responsesPage = responsesPage + 1)"
        >
          다음
        </button>
      </div>

      <div v-if="selectedDetail" class="admin-detail-wrapper">
        <div class="result-detail-header">
          <h3 class="result-subtitle">선택한 응답 상세</h3>
          <button
            v-if="selectedDetail.resultId"
            type="button"
            class="primary-btn"
            :disabled="resultPdfDownloading"
            @click="downloadResultPdf"
          >
            {{ resultPdfDownloading ? '다운로드 중…' : 'PDF 다운로드' }}
          </button>
        </div>
        <div class="result-summary detail-result-summary">
          <div class="summary-item">
            <span class="summary-label">검사</span>
            <span class="summary-value">{{ selectedDetail.assessmentName ?? '-' }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">응답 ID</span>
            <span class="summary-value">{{ selectedDetail.sessionId }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">제출 시각</span>
            <span class="summary-value">{{ formatDateTime(selectedDetail.submittedAt) }}</span>
          </div>
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
            <template v-if="adminScaleGroups.length">
              <template v-for="group in adminScaleGroups" :key="group.groupLabel">
                <tr class="scale-group-header">
                  <td colspan="3">{{ group.groupLabel }}</td>
                </tr>
                <tr v-for="code in group.scaleCodes" :key="code">
                  <td>{{ adminScaleLabel(code) }}</td>
                  <td>{{ (selectedDetail.scaleRawScores[code] ?? 0).toFixed(1) }}</td>
                  <td>{{ (selectedDetail.scaleTScores[code] ?? 0).toFixed(1) }}</td>
                </tr>
              </template>
            </template>
            <template v-else-if="adminScaleOrder.length">
              <tr v-for="code in adminScaleOrder" :key="code">
                <td>{{ adminScaleLabel(code) }}</td>
                <td>{{ (selectedDetail.scaleRawScores[code] ?? 0).toFixed(1) }}</td>
                <td>{{ (selectedDetail.scaleTScores[code] ?? 0).toFixed(1) }}</td>
              </tr>
            </template>
            <template v-else>
              <tr v-for="(raw, code) in selectedDetail.scaleRawScores" :key="code">
                <td>{{ code }}</td>
                <td>{{ raw.toFixed(1) }}</td>
                <td>{{ (selectedDetail.scaleTScores[code] ?? 0).toFixed(1) }}</td>
              </tr>
            </template>
          </tbody>
        </table>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { Chart } from 'chart.js/auto'
import {
  fetchAdminDashboard,
  fetchAccessLogs,
  fetchReliability,
  fetchAdminResponses,
  fetchAdminResponseDetail,
  fetchAdminReference,
  downloadAdminSummaryPdf,
  downloadAdminReferencePdf,
  downloadAdminResultPdf,
  type AccessLogCount,
  type AdminDashboardData,
  type AdminResponseSummary,
  type AdminResponseDetail,
  type AssessmentReference,
  type NormRow,
  type AdminDashboardChartsPayload,
} from '../api'

const props = defineProps<{
  token: string | null
}>()

const loading = ref(true)
const error = ref<string | null>(null)
const data = ref<AdminDashboardData | null>(null)
const logs = ref<AccessLogCount[]>([])
const responses = ref<AdminResponseSummary[]>([])
const selectedDetail = ref<AdminResponseDetail | null>(null)
const reliability = ref<Record<string, number>>({})
const referenceList = ref<AssessmentReference[]>([])
const referenceLoading = ref(false)
const summaryDownloading = ref(false)
const referenceDownloading = ref(false)
const resultPdfDownloading = ref(false)

const pageSize = 10
const logsPage = ref(1)
const responsesPage = ref(1)

const logsTotalPages = computed(() =>
  logs.value.length ? Math.max(1, Math.ceil(logs.value.length / pageSize)) : 1,
)
const responsesTotalPages = computed(() =>
  responses.value.length ? Math.max(1, Math.ceil(responses.value.length / pageSize)) : 1,
)

const pagedLogs = computed(() => {
  const start = (logsPage.value - 1) * pageSize
  return logs.value.slice(start, start + pageSize)
})

const pagedResponses = computed(() => {
  const start = (responsesPage.value - 1) * pageSize
  return responses.value.slice(start, start + pageSize)
})

const adminScaleOrder = computed(() =>
  selectedDetail.value?.scaleOrder?.length ? selectedDetail.value.scaleOrder! : [],
)
const adminScaleGroups = computed(() =>
  selectedDetail.value?.scaleGroups?.length ? selectedDetail.value.scaleGroups! : [],
)
function adminScaleLabel(code: string) {
  const d = selectedDetail.value
  if (!d) return code
  const name = d.scaleDisplayNames?.[code]
  return name ? `${name} (${code})` : code
}

function refNormsForGroup(ref: AssessmentReference, group: { scaleCodes: string[] }): NormRow[] {
  return group.scaleCodes
    .map((code) => ref.norms.find((n) => n.scaleCode === code))
    .filter((n): n is NormRow => n != null)
}
function refTotalNorm(ref: AssessmentReference): NormRow | null {
  return ref.norms.find((n) => n.scaleCode === 'TOTAL') ?? null
}

const chartAssessmentRef = ref<HTMLCanvasElement | null>(null)
const chartAvgScoreRef = ref<HTMLCanvasElement | null>(null)
const chartReliabilityRef = ref<HTMLCanvasElement | null>(null)
const chartTScoreRef = ref<HTMLCanvasElement | null>(null)

let chartInstances: Chart<'bar'>[] = []

function destroyCharts() {
  chartInstances.forEach((c) => c.destroy())
  chartInstances = []
}

function updateCharts() {
  destroyCharts()

  const resp = responses.value
  const rel = reliability.value

  // 1. 검사별 응답 수
  const byAssessment: Record<string, number> = {}
  resp.forEach((r) => {
    const name = r.assessmentName ?? '(미지정)'
    byAssessment[name] = (byAssessment[name] ?? 0) + 1
  })
  const assessmentLabels = Object.keys(byAssessment)
  const assessmentData = assessmentLabels.map((k) => byAssessment[k])

  if (chartAssessmentRef.value) {
    const chart = new Chart(chartAssessmentRef.value, {
      type: 'bar',
      data: {
        labels: assessmentLabels.length ? assessmentLabels : ['(데이터 없음)'],
        datasets: [{ label: '응답 수', data: assessmentData.length ? assessmentData : [0], backgroundColor: 'rgba(59, 130, 246, 0.7)' }],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: false } },
        scales: {
          y: { beginAtZero: true, ticks: { stepSize: 1 } },
        },
      },
    })
    chartInstances.push(chart)
    chart.resize()
  }

  // 2. 검사별 평균 총점
  const avgByAssessment: Record<string, { sum: number; n: number }> = {}
  resp.forEach((r) => {
    const name = r.assessmentName ?? '(미지정)'
    if (!avgByAssessment[name]) avgByAssessment[name] = { sum: 0, n: 0 }
    const raw = r.totalRawScore
    if (raw != null && !Number.isNaN(raw)) {
      avgByAssessment[name].sum += raw
      avgByAssessment[name].n += 1
    }
  })
  const avgLabels = Object.keys(avgByAssessment)
  const avgData = avgLabels.map((k) =>
    avgByAssessment[k].n > 0 ? avgByAssessment[k].sum / avgByAssessment[k].n : 0
  )
  if (chartAvgScoreRef.value) {
    const chart = new Chart(chartAvgScoreRef.value, {
      type: 'bar',
      data: {
        labels: avgLabels.length ? avgLabels : ['(데이터 없음)'],
        datasets: [
          { label: '평균 총점', data: avgData.length ? avgData : [0], backgroundColor: 'rgba(34, 197, 94, 0.7)' },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: false } },
        scales: {
          y: { beginAtZero: true },
        },
      },
    })
    chartInstances.push(chart)
    chart.resize()
  }

  // 3. 척도별 신뢰도 α
  const relLabels = Object.keys(rel)
  const relData = relLabels.map((k) => rel[k])
  if (chartReliabilityRef.value) {
    const chart = new Chart(chartReliabilityRef.value, {
      type: 'bar',
      data: {
        labels: relLabels.length ? relLabels : ['(데이터 없음)'],
        datasets: [{ label: 'α', data: relData.length ? relData : [0], backgroundColor: 'rgba(168, 85, 247, 0.7)' }],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: false } },
        scales: {
          y: { min: 0, max: 1 },
        },
      },
    })
    chartInstances.push(chart)
    chart.resize()
  }

  // 4. 총 T점수 분포 (히스토그램)
  const tScores = resp.map((r) => r.totalTScore).filter((t): t is number => t != null && !Number.isNaN(t))
  const bins = [25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75]
  const hist = new Array(bins.length).fill(0)
  tScores.forEach((t) => {
    for (let i = 0; i < bins.length - 1; i++) {
      if (t >= bins[i] && t < bins[i + 1]) {
        hist[i]++
        break
      }
    }
    if (t >= bins[bins.length - 1]) hist[bins.length - 1]++
  })
  const binLabels = bins.map((b, i) => (i < bins.length - 1 ? `${b}~${bins[i + 1]}` : `${b}+`))

  if (chartTScoreRef.value) {
    const chart = new Chart(chartTScoreRef.value, {
      type: 'bar',
      data: {
        labels: binLabels,
        datasets: [{ label: '응답 수', data: hist, backgroundColor: 'rgba(245, 158, 11, 0.7)' }],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: false } },
        scales: {
          y: { beginAtZero: true, ticks: { stepSize: 1 } },
        },
      },
    })
    chartInstances.push(chart)
    chart.resize()
  }
}

async function loadReference() {
  if (!props.token) return
  referenceLoading.value = true
  try {
    referenceList.value = await fetchAdminReference(props.token)
  } catch (e) {
    referenceList.value = []
  } finally {
    referenceLoading.value = false
  }
}

async function load() {
  if (!props.token) {
    error.value = '인증 토큰이 없습니다.'
    loading.value = false
    return
  }
  loading.value = true
  error.value = null
  try {
    data.value = await fetchAdminDashboard(props.token)
    logs.value = await fetchAccessLogs(props.token)
    responses.value = await fetchAdminResponses(props.token)
    logsPage.value = 1
    responsesPage.value = 1
    reliability.value = await fetchReliability(props.token)
    loadReference()
    await nextTick()
    setTimeout(updateCharts, 150)
  } catch (e) {
    error.value = '대시보드 정보를 불러오지 못했습니다.'
  } finally {
    loading.value = false
  }
}

onMounted(load)
onBeforeUnmount(destroyCharts)

watch(
  () => props.token,
  () => {
    load()
  },
)

function formatDateTime(value: string | null) {
  if (value == null) return '-'
  return value.replace('T', ' ')
}

async function selectResponse(r: AdminResponseSummary) {
  if (!props.token) return
  try {
    selectedDetail.value = await fetchAdminResponseDetail(props.token, r.sessionId)
  } catch (e) {
    // 단순 실패 시는 무시
  }
}

async function downloadAdminSummary() {
  if (!props.token || summaryDownloading.value) return
  const charts: AdminDashboardChartsPayload = {
    chartAssessment: chartAssessmentRef.value ? chartAssessmentRef.value.toDataURL('image/png') : null,
    chartAvgScore: chartAvgScoreRef.value ? chartAvgScoreRef.value.toDataURL('image/png') : null,
    chartReliability: chartReliabilityRef.value ? chartReliabilityRef.value.toDataURL('image/png') : null,
    chartTScore: chartTScoreRef.value ? chartTScoreRef.value.toDataURL('image/png') : null,
  }
  summaryDownloading.value = true
  try {
    await downloadAdminSummaryPdf(props.token, charts)
  } catch (e) {
    // 실패 시 무시 또는 토스트 가능
  } finally {
    summaryDownloading.value = false
  }
}

async function downloadAdminReference() {
  if (!props.token || referenceDownloading.value) return
  referenceDownloading.value = true
  try {
    await downloadAdminReferencePdf(props.token)
  } catch (e) {
    // 실패 시 무시 또는 토스트 가능
  } finally {
    referenceDownloading.value = false
  }
}

async function downloadResultPdf() {
  if (!props.token || !selectedDetail.value?.resultId || resultPdfDownloading.value) return
  resultPdfDownloading.value = true
  try {
    await downloadAdminResultPdf(props.token, selectedDetail.value.resultId)
  } catch (e) {
    // 실패 시 무시
  } finally {
    resultPdfDownloading.value = false
  }
}
</script>

<style scoped>
.charts-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-top: 12px;
}
@media (max-width: 700px) {
  .charts-grid {
    grid-template-columns: 1fr;
  }
  .admin-detail-wrapper {
    overflow-x: auto;
  }
  .detail-result-summary {
    flex-direction: column;
  }
  .detail-result-summary .summary-item {
    width: 100%;
  }
}
.chart-wrap {
  background: var(--card-bg, #fff);
  border-radius: 8px;
  padding: 12px;
  min-width: 0;
}
.chart-container {
  position: relative;
  width: 100%;
  height: 200px;
  min-height: 200px;
}
.chart-container canvas {
  display: block;
  width: 100% !important;
  height: 200px !important;
}
.chart-title {
  font-size: 0.95rem;
  margin: 0 0 8px 0;
  color: var(--text-secondary, #475569);
}
.ref-t-score-explanation {
  margin-bottom: 16px;
  padding: 12px 16px;
  background: var(--card-bg, #f8fafc);
  border-radius: 8px;
  border: 1px solid var(--border-color, #e2e8f0);
}
.ref-t-score-explanation .ref-subtitle { margin-top: 0; }
.ref-t-score-explanation p { margin: 8px 0 0 0; font-size: 0.9rem; line-height: 1.5; }
.ref-simple-notice {
  margin-top: 12px;
  padding: 8px 12px;
  background: var(--bg-notice, #eff6ff);
  border-left: 3px solid var(--border-notice, #3b82f6);
  border-radius: 4px;
  font-size: 0.85rem;
  color: var(--text-secondary, #475569);
}
.ref-loading {
  color: var(--text-secondary, #475569);
  padding: 12px 0;
}
.reference-list {
  margin-top: 8px;
}
.admin-detail-wrapper {
  margin-top: 16px;
}
.result-detail-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 12px;
}
.result-detail-header .result-subtitle { margin: 0; }
.reference-block {
  border: 1px solid var(--border-color, #e2e8f0);
  border-radius: 8px;
  margin-bottom: 12px;
  overflow: hidden;
}
.reference-block summary {
  padding: 12px 16px;
  cursor: pointer;
  font-weight: 600;
  background: var(--card-bg, #f8fafc);
}
.reference-block[open] summary {
  border-bottom: 1px solid var(--border-color, #e2e8f0);
}
.ref-norms,
.ref-guide {
  padding: 12px 16px;
}
.ref-subtitle {
  font-size: 0.9rem;
  margin: 0 0 8px 0;
  color: var(--text-secondary, #475569);
}
.ref-norms-single {
  margin: 0;
  font-size: 0.9rem;
}
.ref-guide-text {
  white-space: pre-wrap;
  word-break: break-word;
  margin: 0;
  font-size: 0.9rem;
  line-height: 1.5;
}
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 8px;
}
.pager-btn {
  padding: 4px 10px;
  font-size: 0.85rem;
  border-radius: 999px;
  border: 1px solid var(--border-color, #e2e8f0);
  background: #fff;
  cursor: pointer;
}
.pager-btn:disabled {
  opacity: 0.5;
  cursor: default;
}
.pager-info {
  font-size: 0.85rem;
  color: var(--text-secondary, #64748b);
}
</style>

