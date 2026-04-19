<template>
  <section class="admin-dashboard">
    <div v-if="loading" class="card">
      <p>{{ t('loadingDashboard') }}</p>
    </div>

    <div v-else-if="error" class="card card-error">
      <p>{{ error }}</p>
    </div>

    <div v-else-if="data" class="card">
      <h2 class="intro-title">{{ t('adminDashboardTitle') }}</h2>
      <p class="intro-desc">{{ t('adminDashboardDesc') }}</p>

      <div class="result-actions" style="margin-bottom: 16px; display: flex; flex-wrap: wrap; gap: 8px;">
        <button
          type="button"
          class="primary-btn"
          @click="downloadAdminSummary"
          :disabled="summaryDownloading"
        >
          {{ t('adminSummaryPdf') }}
        </button>
        <button
          type="button"
          class="secondary-btn"
          @click="downloadAdminReference"
          :disabled="referenceDownloading"
        >
          {{ t('adminReferencePdf') }}
        </button>
      </div>

      <div class="result-summary">
        <div class="summary-item">
          <span class="summary-label">{{ t('adminTotalCompleted') }}</span>
          <span class="summary-value">{{ data.totalCompleted }}</span>
        </div>
        <div class="summary-item">
          <span class="summary-label">{{ t('adminTotalResults') }}</span>
          <span class="summary-value">{{ data.totalResults }}</span>
        </div>
        <div class="summary-item">
          <span class="summary-label">{{ t('adminTodayCompleted') }}</span>
          <span class="summary-value">{{ data.todayCompleted }}</span>
        </div>
      </div>

      <h3 class="result-subtitle" style="margin-top: 20px;">{{ t('adminBoardSection') }}</h3>
      <div class="card" style="margin-bottom: 20px;">
        <div style="display: flex; justify-content: flex-end; margin-bottom: 10px">
          <button type="button" class="secondary-btn" @click="loadBoard">{{ t('adminBoardRefresh') }}</button>
        </div>
        <p v-if="boardError" class="card-error" style="padding: 8px 0">{{ boardError }}</p>
        <div v-else-if="boardLoading" style="padding: 12px">
          <p>{{ t('boardLoadingList') }}</p>
        </div>
        <div v-else-if="boardRows.length === 0" style="padding: 12px">
          <p>{{ t('boardEmpty') }}</p>
        </div>
        <div v-else style="overflow-x: auto">
          <table class="scale-table admin-board-table">
            <thead>
              <tr>
                <th>{{ t('adminBoardColTitle') }}</th>
                <th>{{ t('adminBoardColDate') }}</th>
                <th>{{ t('adminBoardColIp') }}</th>
                <th>{{ t('adminBoardColReply') }}</th>
                <th>{{ t('adminBoardColStatus') }}</th>
                <th style="min-width: 220px"> </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="row in boardRows" :key="row.id">
                <td>{{ row.title }}</td>
                <td>{{ formatDateTime(row.createdAt) }}</td>
                <td class="admin-board-ip">{{ row.submitterIp?.trim() || '—' }}</td>
                <td>{{ row.hasAdminReply ? '✓' : '—' }}</td>
                <td>{{ row.hidden ? t('adminBoardHiddenBadge') : t('adminBoardVisibleBadge') }}</td>
                <td>
                  <div style="display: flex; flex-wrap: wrap; gap: 6px">
                    <button type="button" class="secondary-btn" @click="openBoardReply(row)">
                      {{ t('adminBoardReply') }}
                    </button>
                    <button
                      v-if="!row.hidden"
                      type="button"
                      class="secondary-btn"
                      @click="toggleBoardHidden(row, true)"
                    >
                      {{ t('adminBoardHide') }}
                    </button>
                    <button
                      v-else
                      type="button"
                      class="secondary-btn"
                      @click="toggleBoardHidden(row, false)"
                    >
                      {{ t('adminBoardShow') }}
                    </button>
                    <button type="button" class="secondary-btn" @click="removeBoardPost(row.id)">
                      {{ t('adminBoardDelete') }}
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <h3 class="result-subtitle" style="margin-top: 20px;">{{ t('adminCharts') }}</h3>
      <div class="charts-grid">
        <div class="chart-wrap">
          <h4 class="chart-title">{{ t('adminChartResponses') }}</h4>
          <div class="chart-container chart-container--hbar">
            <canvas ref="chartAssessmentRef"></canvas>
          </div>
        </div>
        <div class="chart-wrap">
          <h4 class="chart-title">{{ t('adminChartAvgScore') }}</h4>
          <div class="chart-container chart-container--hbar">
            <canvas ref="chartAvgScoreRef"></canvas>
          </div>
        </div>
        <div class="chart-wrap">
          <h4 class="chart-title">{{ t('adminChartDailySubmissions', { days: dailySubmissionWindowDays }) }}</h4>
          <p class="chart-subtitle">{{ t('adminChartDailySubmissionsHint') }}</p>
          <div class="chart-container">
            <canvas ref="chartDailySubmissionsRef"></canvas>
          </div>
        </div>
        <div class="chart-wrap">
          <h4 class="chart-title">{{ t('adminChartTScoreDist') }}</h4>
          <div class="chart-container">
            <canvas ref="chartTScoreRef"></canvas>
          </div>
        </div>
      </div>

      <h3 class="result-subtitle" style="margin-top: 20px;">{{ t('adminReferenceTitle') }}</h3>
      <div class="ref-t-score-explanation">
        <h4 class="ref-subtitle">{{ t('tScoreTitle') }}</h4>
        <p>{{ t('adminTScoreDesc1') }}</p>
        <p>{{ t('adminTScoreDesc2') }}</p>
        <p class="ref-simple-notice">{{ t('adminSimpleNotice') }}</p>
      </div>
      <div v-if="referenceLoading" class="ref-loading">{{ t('adminReferenceLoading') }}</div>
      <div v-else class="reference-list">
        <details
          v-for="ref in referenceList"
          :key="ref.assessmentName"
          class="reference-block"
        >
          <summary>{{ ref.assessmentName }}</summary>
          <div v-if="ref.norms.length" class="ref-norms">
            <h4 class="ref-subtitle">{{ t('normsTitle') }}</h4>
            <template v-if="ref.norms.length === 1">
              <p class="ref-norms-single">
                {{ ref.norms[0].scaleName || ref.norms[0].scaleCode }}:
                {{ t('mean') }} {{ ref.norms[0].mean != null ? ref.norms[0].mean.toFixed(2) : '-' }},
                {{ t('sd') }} {{ ref.norms[0].sd != null ? ref.norms[0].sd.toFixed(2) : '-' }}
              </p>
            </template>
            <table v-else class="scale-table">
              <thead>
                <tr>
                  <th>{{ t('scale') }}</th>
                  <th>{{ t('mean') }}</th>
                  <th>{{ t('sd') }}</th>
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
                    <td>{{ refTotalNorm(ref)!.scaleName || t('total') }}</td>
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
            <h4 class="ref-subtitle">{{ t('interpretationGuide') }}</h4>
            <pre class="ref-guide-text">{{ ref.interpretationGuide }}</pre>
          </div>
        </details>
        <p v-if="!referenceList.length && !referenceLoading">{{ t('adminReferenceNone') }}</p>
      </div>

      <h3 class="result-subtitle" style="margin-top: 20px;">{{ t('accessLogs') }}</h3>
      <table class="scale-table">
        <thead>
          <tr>
            <th>IP</th>
            <th>{{ t('date') }}</th>
            <th>{{ t('event') }}</th>
            <th>{{ t('count') }}</th>
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
            <td colspan="4">{{ t('noLogs') }}</td>
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
          {{ t('prev') }}
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
          {{ t('next') }}
        </button>
      </div>

      <h3 class="result-subtitle" style="margin-top: 20px;">{{ t('responsesTitle') }}</h3>
      <table class="scale-table">
        <thead>
          <tr>
            <th>{{ t('assessment') }}</th>
            <th>{{ t('responseId') }}</th>
            <th>{{ t('submittedAt') }}</th>
            <th>{{ t('totalRawScore') }}</th>
            <th>{{ t('totalTScore') }}</th>
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
            <td colspan="5">{{ t('noResponses') }}</td>
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
          {{ t('prev') }}
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
          {{ t('next') }}
        </button>
      </div>

      <div v-if="selectedDetail" class="admin-detail-wrapper">
        <div class="result-detail-header">
          <h3 class="result-subtitle">{{ t('selectedResponseDetail') }}</h3>
          <button
            v-if="selectedDetail.resultId"
            type="button"
            class="primary-btn"
            :disabled="resultPdfDownloading"
            @click="downloadResultPdf"
          >
            {{ resultPdfDownloading ? t('downloading') : t('downloadPdf') }}
          </button>
        </div>
        <div class="result-summary detail-result-summary">
          <div class="summary-item">
            <span class="summary-label">{{ t('assessment') }}</span>
            <span class="summary-value">{{ selectedDetail.assessmentName ?? '-' }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">{{ t('responseId') }}</span>
            <span class="summary-value">{{ selectedDetail.sessionId }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">{{ t('submittedAt') }}</span>
            <span class="summary-value">{{ formatDateTime(selectedDetail.submittedAt) }}</span>
          </div>
        </div>

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

    <div
      v-if="boardReplyOpen"
      class="admin-board-reply-overlay"
      role="dialog"
      aria-modal="true"
      :aria-labelledby="'board-reply-title'"
      @click.self="closeBoardReply"
    >
      <div class="admin-board-reply-dialog card">
        <h3 id="board-reply-title" class="result-subtitle">{{ t('adminBoardReplyModalTitle') }}</h3>
        <p v-if="boardReplyLoading" style="padding: 12px 0">{{ t('boardLoadingList') }}</p>
        <template v-else>
          <p v-if="boardReplyDetail" class="admin-board-reply-meta">
            <strong>{{ boardReplyDetail.title }}</strong>
          </p>
          <p v-if="boardReplyDetail" class="admin-board-reply-body-preview">{{ boardReplyDetail.body }}</p>
          <label class="admin-board-reply-label">
            <span>{{ t('adminBoardReplyPlaceholder') }}</span>
            <textarea
              v-model="boardReplyDraft"
              class="admin-board-reply-textarea"
              rows="8"
              maxlength="8000"
            />
          </label>
          <p
            v-if="boardReplyMessage"
            :class="boardReplyMessageIsError ? 'card-error' : 'admin-board-reply-ok'"
            style="margin: 8px 0 0"
          >
            {{ boardReplyMessage }}
          </p>
          <div class="admin-board-reply-actions">
            <button type="button" class="primary-btn" :disabled="boardReplySaving" @click="saveBoardReply">
              {{ boardReplySaving ? t('boardSubmitting') : t('adminBoardReplySave') }}
            </button>
            <button type="button" class="secondary-btn" :disabled="boardReplySaving" @click="clearBoardReply">
              {{ t('adminBoardReplyClear') }}
            </button>
            <button type="button" class="secondary-btn" :disabled="boardReplySaving" @click="closeBoardReply">
              {{ t('adminBoardReplyClose') }}
            </button>
          </div>
        </template>
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
  fetchAdminResponses,
  fetchAdminResponseDetail,
  fetchAdminReference,
  downloadAdminSummaryPdf,
  downloadAdminReferencePdf,
  downloadAdminResultPdf,
  fetchAdminBoardPosts,
  fetchAdminBoardPostDetail,
  patchAdminBoardPostHidden,
  patchAdminBoardPostReply,
  deleteAdminBoardPost,
  type AccessLogCount,
  type BoardAdminPostRow,
  type BoardAdminPostDetail,
  type AdminDashboardData,
  type AdminResponseSummary,
  type AdminResponseDetail,
  type AssessmentReference,
  type NormRow,
  type AdminDashboardChartsPayload,
} from '../api'
import { useI18n } from '../i18n'
const props = defineProps<{
  token: string | null
}>()

const loading = ref(true)
const error = ref<string | null>(null)
const data = ref<AdminDashboardData | null>(null)
const logs = ref<AccessLogCount[]>([])
const responses = ref<AdminResponseSummary[]>([])
const selectedDetail = ref<AdminResponseDetail | null>(null)
const referenceList = ref<AssessmentReference[]>([])
const referenceLoading = ref(false)
const summaryDownloading = ref(false)
const referenceDownloading = ref(false)
const resultPdfDownloading = ref(false)
const boardRows = ref<BoardAdminPostRow[]>([])
const boardLoading = ref(false)
const boardError = ref<string | null>(null)
const boardReplyOpen = ref(false)
const boardReplyLoading = ref(false)
const boardReplySaving = ref(false)
const boardReplyDetail = ref<BoardAdminPostDetail | null>(null)
const boardReplyDraft = ref('')
const boardReplyMessage = ref<string | null>(null)
const boardReplyMessageIsError = ref(false)
const { t, locale } = useI18n()

/** 제출 추이 차트: 오늘 포함 역산 일수 */
const dailySubmissionWindowDays = 30

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
const chartDailySubmissionsRef = ref<HTMLCanvasElement | null>(null)
const chartTScoreRef = ref<HTMLCanvasElement | null>(null)

let chartInstances: Chart<'bar'>[] = []

function destroyCharts() {
  chartInstances.forEach((c) => c.destroy())
  chartInstances = []
}

function localYmd(d: Date): string {
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${y}-${m}-${day}`
}

/** 오늘 0시 기준 최근 `days`일(포함)의 일별 완료 건수 */
function buildDailySubmissionSeries(resp: AdminResponseSummary[], days: number) {
  const end = new Date()
  end.setHours(0, 0, 0, 0)
  const keys: string[] = []
  for (let i = days - 1; i >= 0; i--) {
    const d = new Date(end)
    d.setDate(d.getDate() - i)
    keys.push(localYmd(d))
  }
  const counts: Record<string, number> = {}
  keys.forEach((k) => {
    counts[k] = 0
  })
  resp.forEach((r) => {
    if (r.submittedAt == null || r.submittedAt === '') return
    const d = new Date(r.submittedAt)
    if (Number.isNaN(d.getTime())) return
    const k = localYmd(d)
    if (counts[k] !== undefined) counts[k]++
  })
  const data = keys.map((k) => counts[k])
  const labels = keys.map((k) => {
    const [, mm, dd] = k.split('-')
    return `${Number(mm)}/${Number(dd)}`
  })
  return { labels, data }
}

function updateCharts() {
  destroyCharts()

  const resp = responses.value

  // 1. 검사별 응답 수
  const byAssessment: Record<string, number> = {}
  resp.forEach((r) => {
    const name = r.assessmentName ?? t('noAssignment')
    byAssessment[name] = (byAssessment[name] ?? 0) + 1
  })
  const assessmentLabels = Object.keys(byAssessment)
  const assessmentData = assessmentLabels.map((k) => byAssessment[k])

  if (chartAssessmentRef.value) {
    const chart = new Chart(chartAssessmentRef.value, {
      type: 'bar',
      data: {
        labels: assessmentLabels.length ? assessmentLabels : [t('noData')],
        datasets: [
          {
            label: t('responseCount'),
            data: assessmentData.length ? assessmentData : [0],
            backgroundColor: 'rgba(0, 40, 142, 0.72)',
          },
        ],
      },
      options: {
        indexAxis: 'y',
        responsive: true,
        maintainAspectRatio: false,
        layout: { padding: { left: 4, right: 20, top: 6, bottom: 6 } },
        plugins: { legend: { display: false } },
        scales: {
          x: {
            beginAtZero: true,
            ticks: { precision: 0 },
            grid: { color: 'rgba(0, 0, 0, 0.06)' },
          },
          y: {
            grid: { display: false },
            ticks: { font: { size: 10 }, autoSkip: false },
          },
        },
      },
    })
    chartInstances.push(chart)
    chart.resize()
  }

  // 2. 검사별 평균 총점
  const avgByAssessment: Record<string, { sum: number; n: number }> = {}
  resp.forEach((r) => {
    const name = r.assessmentName ?? t('noAssignment')
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
        labels: avgLabels.length ? avgLabels : [t('noData')],
        datasets: [
          {
            label: t('averageTotalScore'),
            data: avgData.length ? avgData : [0],
            backgroundColor: 'rgba(13, 148, 136, 0.72)',
          },
        ],
      },
      options: {
        indexAxis: 'y',
        responsive: true,
        maintainAspectRatio: false,
        layout: { padding: { left: 4, right: 20, top: 6, bottom: 6 } },
        plugins: { legend: { display: false } },
        scales: {
          x: {
            beginAtZero: true,
            grid: { color: 'rgba(0, 0, 0, 0.06)' },
          },
          y: {
            grid: { display: false },
            ticks: { font: { size: 10 }, autoSkip: false },
          },
        },
      },
    })
    chartInstances.push(chart)
    chart.resize()
  }

  // 3. 최근 N일 일별 제출(완료) 건수
  const daily = buildDailySubmissionSeries(resp, dailySubmissionWindowDays)
  if (chartDailySubmissionsRef.value) {
    const chart = new Chart(chartDailySubmissionsRef.value, {
      type: 'bar',
      data: {
        labels: daily.labels,
        datasets: [
          {
            label: t('dailySubmissionCount'),
            data: daily.data,
            backgroundColor: 'rgba(93, 95, 239, 0.65)',
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        layout: { padding: { left: 4, right: 8, top: 6, bottom: 4 } },
        plugins: { legend: { display: false } },
        scales: {
          x: {
            grid: { display: false },
            ticks: { maxRotation: 45, minRotation: 0, autoSkip: true, maxTicksLimit: 16, font: { size: 9 } },
          },
          y: {
            beginAtZero: true,
            ticks: { precision: 0, stepSize: 1 },
            grid: { color: 'rgba(0, 0, 0, 0.06)' },
          },
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
        datasets: [
          { label: t('responseCount'), data: hist, backgroundColor: 'rgba(180, 83, 9, 0.68)' },
        ],
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

async function loadBoard() {
  if (!props.token) return
  boardLoading.value = true
  boardError.value = null
  try {
    const p = await fetchAdminBoardPosts(props.token, 0, 50)
    boardRows.value = p.content
  } catch {
    boardError.value = t('adminBoardLoadError')
    boardRows.value = []
  } finally {
    boardLoading.value = false
  }
}

async function toggleBoardHidden(row: BoardAdminPostRow, hidden: boolean) {
  if (!props.token) return
  try {
    await patchAdminBoardPostHidden(props.token, row.id, hidden)
    await loadBoard()
  } catch {
    boardError.value = t('adminBoardLoadError')
  }
}

async function removeBoardPost(id: string) {
  if (!props.token) return
  if (!window.confirm(t('adminBoardDeleteConfirm'))) return
  try {
    await deleteAdminBoardPost(props.token, id)
    await loadBoard()
  } catch {
    boardError.value = t('adminBoardLoadError')
  }
}

function closeBoardReply() {
  boardReplyOpen.value = false
  boardReplyDetail.value = null
  boardReplyDraft.value = ''
  boardReplyMessage.value = null
  boardReplyMessageIsError.value = false
  boardReplyLoading.value = false
  boardReplySaving.value = false
}

async function openBoardReply(row: BoardAdminPostRow) {
  if (!props.token) return
  boardReplyMessage.value = null
  boardReplyMessageIsError.value = false
  boardReplyOpen.value = true
  boardReplyLoading.value = true
  boardReplyDetail.value = null
  boardReplyDraft.value = ''
  try {
    const d = await fetchAdminBoardPostDetail(props.token, row.id)
    boardReplyDetail.value = d
    boardReplyDraft.value = d.adminReply ?? ''
  } catch {
    boardReplyMessage.value = t('adminBoardReplyError')
    boardReplyMessageIsError.value = true
  } finally {
    boardReplyLoading.value = false
  }
}

async function saveBoardReply() {
  if (!props.token || !boardReplyDetail.value) return
  boardReplySaving.value = true
  boardReplyMessage.value = null
  boardReplyMessageIsError.value = false
  try {
    await patchAdminBoardPostReply(props.token, boardReplyDetail.value.id, boardReplyDraft.value.trim())
    await loadBoard()
    closeBoardReply()
  } catch {
    boardReplyMessage.value = t('adminBoardReplyError')
    boardReplyMessageIsError.value = true
  } finally {
    boardReplySaving.value = false
  }
}

async function clearBoardReply() {
  if (!props.token || !boardReplyDetail.value) return
  if (!window.confirm(t('adminBoardReplyClearConfirm'))) return
  boardReplySaving.value = true
  boardReplyMessage.value = null
  boardReplyMessageIsError.value = false
  try {
    await patchAdminBoardPostReply(props.token, boardReplyDetail.value.id, '')
    boardReplyDraft.value = ''
    await loadBoard()
    const d = await fetchAdminBoardPostDetail(props.token, boardReplyDetail.value.id)
    boardReplyDetail.value = d
    boardReplyMessage.value = t('adminBoardReplySaved')
    boardReplyMessageIsError.value = false
  } catch {
    boardReplyMessage.value = t('adminBoardReplyError')
    boardReplyMessageIsError.value = true
  } finally {
    boardReplySaving.value = false
  }
}

async function load() {
  if (!props.token) {
    error.value = t('tokenMissing')
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
    loadReference()
    void loadBoard()
    await nextTick()
    setTimeout(updateCharts, 150)
  } catch (e) {
    error.value = t('dashboardLoadError')
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

watch(locale, () => {
  void nextTick().then(() => setTimeout(updateCharts, 150))
})

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
    chartDailySubmissions: chartDailySubmissionsRef.value
      ? chartDailySubmissionsRef.value.toDataURL('image/png')
      : null,
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
/** 검사명 등 긴 가로 막대 라벨용 (PDF 캡처 시 잘림 완화) */
.chart-container--hbar {
  height: 280px;
  min-height: 280px;
}
.chart-container canvas {
  display: block;
  width: 100% !important;
  height: 200px !important;
}
.chart-container--hbar canvas {
  height: 280px !important;
}
.chart-title {
  font-size: 0.95rem;
  margin: 0 0 8px 0;
  color: var(--text-secondary, #475569);
}
.chart-subtitle {
  font-size: 0.78rem;
  margin: -4px 0 10px 0;
  color: var(--text-muted, #64748b);
  line-height: 1.35;
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
  background: var(--card-bg, #fff);
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
.admin-board-ip {
  font-size: 0.85rem;
  white-space: nowrap;
  max-width: 140px;
  overflow: hidden;
  text-overflow: ellipsis;
}
.admin-board-reply-overlay {
  position: fixed;
  inset: 0;
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background: rgba(15, 23, 42, 0.45);
}
.admin-board-reply-dialog {
  box-sizing: border-box;
  width: min(560px, 100%);
  max-width: 100%;
  min-width: 0;
  max-height: min(90vh, 720px);
  overflow-x: hidden;
  overflow-y: auto;
  padding: 20px;
}
.admin-board-reply-meta {
  margin: 8px 0;
  font-size: 0.95rem;
  max-width: 100%;
  overflow-wrap: anywhere;
  word-break: break-word;
}
.admin-board-reply-body-preview {
  margin: 0 0 12px;
  font-size: 0.85rem;
  color: var(--text-secondary, #475569);
  white-space: pre-wrap;
  overflow-wrap: anywhere;
  word-break: break-word;
  max-width: 100%;
  max-height: 120px;
  overflow-x: hidden;
  overflow-y: auto;
  border: 1px solid var(--border-color, #e2e8f0);
  border-radius: 8px;
  padding: 8px 10px;
  background: var(--card-bg, #f8fafc);
}
.admin-board-reply-label {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-size: 0.9rem;
  font-weight: 600;
  margin-top: 8px;
  min-width: 0;
  max-width: 100%;
}
.admin-board-reply-textarea {
  box-sizing: border-box;
  width: 100%;
  max-width: 100%;
  min-width: 0;
  font-family: inherit;
  font-size: 0.9rem;
  padding: 10px 12px;
  border-radius: 8px;
  border: 1px solid var(--border-color, #e2e8f0);
  resize: vertical;
}
.admin-board-reply-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 14px;
}
.admin-board-reply-ok {
  color: #166534;
  font-size: 0.9rem;
}
</style>

