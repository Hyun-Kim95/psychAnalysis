import axios from 'axios'

export interface AssessmentSummary {
  id: number
  name: string
  description: string | null
  itemCount?: number
}

export interface AssessmentItemChoice {
  value: number
  label: string
}

export interface AssessmentItem {
  id: number
  itemNumber: number
  text: string
  scaleCode: string
  choices: AssessmentItemChoice[]
}

export interface AssessmentItemsResponse {
  assessmentId: number
  items: AssessmentItem[]
}

export interface ApiResponse<T> {
  success: boolean
  data: T
  errorCode?: string
  message?: string
}

export async function fetchAssessments() {
  const res = await axios.get<ApiResponse<AssessmentSummary[]>>('/api/assessments')
  return res.data.data
}

export async function fetchCurrentAssessment() {
  const res = await axios.get<ApiResponse<AssessmentSummary>>('/api/assessments/current')
  return res.data.data
}

export async function fetchAssessment(assessmentId: number) {
  const res = await axios.get<ApiResponse<AssessmentSummary>>(`/api/assessments/${assessmentId}`)
  return res.data.data
}

export async function fetchCurrentAssessmentItems() {
  const res = await axios.get<ApiResponse<AssessmentItemsResponse>>('/api/assessments/current/items')
  return res.data.data
}

export async function fetchAssessmentItems(assessmentId: number) {
  const res = await axios.get<ApiResponse<AssessmentItemsResponse>>(`/api/assessments/${assessmentId}/items`)
  return res.data.data
}

export async function createSession(assessmentId: number, groupCode?: string) {
  const res = await axios.post<ApiResponse<{ responseSessionId: string }>>('/api/response-sessions', {
    assessmentId,
    groupCode,
  })
  return res.data.data
}

export async function submitAnswers(sessionId: string, answers: { itemId: number; value: number }[]) {
  const res = await axios.post<ApiResponse<{ resultId: string }>>(
    `/api/response-sessions/${sessionId}/submit`,
    { answers },
  )
  return res.data.data
}

/** TCI 엑셀 기준 척도 표시 순서 */
export const TCI_SCALE_ORDER = ['NS', 'HA', 'RD', 'P', 'SD', 'C', 'ST'] as const

export interface ResultViewData {
  /** 실시한 검사명 */
  assessmentName?: string
  totalRawScore: number | null
  totalTScore: number | null
  scaleRawScores: Record<string, number>
  scaleTScores: Record<string, number>
  /** 검사별 척도 표시 순서 */
  scaleOrder?: string[]
  /** 척도 코드 → 한글명 */
  scaleDisplayNames?: Record<string, string>
  /** 척도 코드 → 점수 기반 해석 문장 (TCI만) */
  scaleInterpretations?: Record<string, string>
}

export async function fetchResult(resultId: string) {
  const res = await axios.get<ApiResponse<ResultViewData>>(`/api/results/${resultId}`)
  return res.data.data
}

export interface AdminLoginResponse {
  token: string
}

export interface AdminDashboardData {
  totalCompleted: number
  totalResults: number
  todayCompleted: number
}

export interface GroupTTestResult {
  nA: number
  nB: number
  meanA: number
  meanB: number
  varA: number
  varB: number
  t: number
  df: number
  pValue: number
}

export async function adminLogin(loginId: string, password: string) {
  const res = await axios.post<ApiResponse<AdminLoginResponse>>('/api/admin/login', {
    loginId,
    password,
  })
  return res.data.data
}

export async function fetchAdminDashboard(token: string) {
  const res = await axios.get<ApiResponse<AdminDashboardData>>('/api/admin/dashboard', {
    headers: { 'X-Admin-Token': token },
  })
  return res.data.data
}

export async function fetchGroupTTest(token: string, metric = 'totalT') {
  const res = await axios.get<ApiResponse<GroupTTestResult | null>>('/api/admin/stats/group-t-test', {
    params: { metric },
    headers: { 'X-Admin-Token': token },
  })
  return res.data.data
}

export async function fetchReliability(token: string) {
  const res = await axios.get<ApiResponse<Record<string, number>>>('/api/admin/stats/reliability', {
    headers: { 'X-Admin-Token': token },
  })
  return res.data.data
}

/** IP·날짜·이벤트별 접속 횟수 */
export interface AccessLogCount {
  id: number
  clientIpMasked: string
  logDate: string
  eventType: string
  count: number
}

export async function fetchAccessLogs(
  token: string,
  params?: { from?: string; to?: string }
) {
  const res = await axios.get<ApiResponse<AccessLogCount[]>>('/api/admin/access-logs', {
    params,
    headers: { 'X-Admin-Token': token },
  })
  return res.data.data
}

export interface AdminResponseSummary {
  sessionId: string
  submittedAt: string | null
  groupCode: string | null
  assessmentName: string | null
  totalRawScore: number | null
  totalTScore: number | null
}

export interface AdminResponseDetail {
  sessionId: string
  resultId: string | null
  submittedAt: string | null
  groupCode: string | null
  assessmentName: string | null
  totalRawScore: number | null
  totalTScore: number | null
  scaleRawScores: Record<string, number>
  scaleTScores: Record<string, number>
}

export async function fetchAdminResponses(token: string) {
  const res = await axios.get<ApiResponse<AdminResponseSummary[]>>('/api/admin/responses', {
    headers: { 'X-Admin-Token': token },
  })
  return res.data.data
}

export async function fetchAdminResponseDetail(token: string, sessionId: string) {
  const res = await axios.get<ApiResponse<AdminResponseDetail>>(`/api/admin/responses/${sessionId}`, {
    headers: { 'X-Admin-Token': token },
  })
  return res.data.data
}

/** 검사별 기준점수(규준) 및 해석 기준 */
export interface NormRow {
  scaleCode: string
  scaleName: string
  mean: number | null
  sd: number | null
}

export interface AssessmentReference {
  assessmentName: string
  norms: NormRow[]
  interpretationGuide: string
}

export async function fetchAdminReference(token: string) {
  const res = await axios.get<ApiResponse<AssessmentReference[]>>('/api/admin/reference', {
    headers: { 'X-Admin-Token': token },
  })
  return res.data.data
}

export interface AdminDashboardChartsPayload {
  chartAssessment?: string | null
  chartAvgScore?: string | null
  chartReliability?: string | null
  chartTScore?: string | null
}

/** 관리자 리포트 - 대시보드 요약 PDF */
export async function downloadAdminSummaryPdf(token: string, charts: AdminDashboardChartsPayload): Promise<void> {
  const res = await axios.post<Blob>('/api/admin/report/summary-pdf', charts, {
    headers: { 'X-Admin-Token': token },
    responseType: 'blob',
  })
  const url = window.URL.createObjectURL(res.data)
  const a = document.createElement('a')
  a.href = url
  a.download = `관리자리포트-요약-${new Date().toISOString().slice(0, 19).replace('T', '_').replace(/:/g, '-')}.pdf`
  a.click()
  window.URL.revokeObjectURL(url)
}

/** 관리자 리포트 - 기준·해석 PDF */
export async function downloadAdminReferencePdf(token: string): Promise<void> {
  const res = await axios.get<Blob>('/api/admin/report/reference-pdf', {
    headers: { 'X-Admin-Token': token },
    responseType: 'blob',
  })
  const url = window.URL.createObjectURL(res.data)
  const a = document.createElement('a')
  a.href = url
  a.download = `관리자리포트-기준해석-${new Date().toISOString().slice(0, 19).replace('T', '_').replace(/:/g, '-')}.pdf`
  a.click()
  window.URL.revokeObjectURL(url)
}

