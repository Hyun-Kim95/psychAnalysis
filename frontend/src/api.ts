import axios from 'axios'

export interface AssessmentSummary {
  id: number
  name: string
  description: string | null
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

export async function fetchCurrentAssessment() {
  const res = await axios.get<ApiResponse<AssessmentSummary>>('/api/assessments/current')
  return res.data.data
}

export async function fetchCurrentAssessmentItems() {
  const res = await axios.get<ApiResponse<AssessmentItemsResponse>>('/api/assessments/current/items')
  return res.data.data
}

export async function createSession(groupCode?: string) {
  const res = await axios.post<ApiResponse<{ responseSessionId: string }>>('/api/response-sessions', {
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

export interface ResultViewData {
  totalRawScore: number | null
  totalTScore: number | null
  scaleRawScores: Record<string, number>
  scaleTScores: Record<string, number>
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

export interface AccessLog {
  id: number
  occurredAt: string
  url: string | null
  userAgent: string | null
  clientIpMasked: string | null
  eventType: string | null
  responseSessionId: string | null
}

export async function fetchAccessLogs(token: string) {
  const res = await axios.get<ApiResponse<AccessLog[]>>('/api/admin/access-logs', {
    headers: { 'X-Admin-Token': token },
  })
  return res.data.data
}

export interface AdminResponseSummary {
  sessionId: string
  submittedAt: string | null
  groupCode: string | null
  totalRawScore: number | null
  totalTScore: number | null
}

export interface AdminResponseDetail {
  sessionId: string
  submittedAt: string | null
  groupCode: string | null
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

