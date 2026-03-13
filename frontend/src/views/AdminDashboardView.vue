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

      <div v-if="groupStats" class="result-summary" style="margin-top: 8px;">
        <div class="summary-item">
          <span class="summary-label">A/B 총 T점수 차이 (A-B)</span>
          <span class="summary-value">
            {{ (groupStats.meanA - groupStats.meanB).toFixed(2) }}
          </span>
        </div>
        <div class="summary-item">
          <span class="summary-label">t(df)</span>
          <span class="summary-value">
            {{ groupStats.t.toFixed(2) }} (df={{ groupStats.df.toFixed(1) }})
          </span>
        </div>
        <div class="summary-item">
          <span class="summary-label">p-value</span>
          <span class="summary-value">
            {{ groupStats.pValue.toFixed(3) }}
          </span>
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

      <h3 class="result-subtitle" style="margin-top: 20px;">최근 접속 로그</h3>
      <table class="scale-table">
        <thead>
          <tr>
            <th>시각</th>
            <th>이벤트</th>
            <th>URL</th>
            <th>IP</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="log in logs" :key="log.id">
            <td>{{ log.occurredAt }}</td>
            <td>{{ log.eventType }}</td>
            <td>{{ log.url }}</td>
            <td>{{ log.clientIpMasked }}</td>
          </tr>
          <tr v-if="!logs.length">
            <td colspan="4">표시할 로그가 없습니다.</td>
          </tr>
        </tbody>
      </table>

      <h3 class="result-subtitle" style="margin-top: 20px;">응답 목록</h3>
      <table class="scale-table">
        <thead>
          <tr>
            <th>응답 ID</th>
            <th>제출 시각</th>
            <th>그룹</th>
            <th>총점</th>
            <th>총 T점수</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="r in responses"
            :key="r.sessionId"
            @click="selectResponse(r)"
            style="cursor: pointer;"
          >
            <td>{{ r.sessionId }}</td>
            <td>{{ r.submittedAt }}</td>
            <td>{{ r.groupCode }}</td>
            <td>{{ r.totalRawScore ?? '-' }}</td>
            <td>{{ r.totalTScore ?? '-' }}</td>
          </tr>
          <tr v-if="!responses.length">
            <td colspan="5">표시할 응답이 없습니다.</td>
          </tr>
        </tbody>
      </table>

      <div v-if="selectedDetail" style="margin-top: 16px;">
        <h3 class="result-subtitle">선택한 응답 상세</h3>
        <div class="result-summary">
          <div class="summary-item">
            <span class="summary-label">응답 ID</span>
            <span class="summary-value">{{ selectedDetail.sessionId }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">제출 시각</span>
            <span class="summary-value">{{ selectedDetail.submittedAt }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">그룹</span>
            <span class="summary-value">{{ selectedDetail.groupCode ?? '-' }}</span>
          </div>
        </div>

        <h4 class="result-subtitle" style="margin-top: 12px;">척도별 점수</h4>
        <table class="scale-table">
          <thead>
            <tr>
              <th>척도</th>
              <th>Raw</th>
              <th>T</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(raw, code) in selectedDetail.scaleRawScores" :key="code">
              <td>{{ code }}</td>
              <td>{{ raw.toFixed(1) }}</td>
              <td>{{ (selectedDetail.scaleTScores[code] ?? 0).toFixed(1) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import {
  fetchAdminDashboard,
  fetchAccessLogs,
  fetchGroupTTest,
  fetchReliability,
  fetchAdminResponses,
  fetchAdminResponseDetail,
  type AccessLog,
  type AdminDashboardData,
  type GroupTTestResult,
  type AdminResponseSummary,
  type AdminResponseDetail,
} from '../api'

const props = defineProps<{
  token: string | null
}>()

const loading = ref(true)
const error = ref<string | null>(null)
const data = ref<AdminDashboardData | null>(null)
const logs = ref<AccessLog[]>([])
const responses = ref<AdminResponseSummary[]>([])
const selectedDetail = ref<AdminResponseDetail | null>(null)
const groupStats = ref<GroupTTestResult | null>(null)
const reliability = ref<Record<string, number>>({})

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
    groupStats.value = await fetchGroupTTest(props.token)
    reliability.value = await fetchReliability(props.token)
  } catch (e) {
    error.value = '대시보드 정보를 불러오지 못했습니다.'
  } finally {
    loading.value = false
  }
}

onMounted(load)

watch(
  () => props.token,
  () => {
    load()
  },
)

async function selectResponse(r: AdminResponseSummary) {
  if (!props.token) return
  try {
    selectedDetail.value = await fetchAdminResponseDetail(props.token, r.sessionId)
  } catch (e) {
    // 단순 실패 시는 무시
  }
}
</script>

