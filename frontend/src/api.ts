import axios from 'axios'
import { locale } from './i18n'

/**
 * 로컬 `npm run dev`에서 `VITE_API_URL`이 localhost/127.0.0.1이면 브라우저→8080 직통이 되어
 * CORS·포트 불일치가 나기 쉬움. 이때는 baseURL을 비워 Vite 프록시(`/api` → 8080)를 쓴다.
 * 배포 빌드·원격 API URL은 그대로 사용한다.
 */
function resolveAxiosBaseURL(): string {
  const raw = typeof import.meta.env.VITE_API_URL === 'string' ? import.meta.env.VITE_API_URL.trim() : ''
  if (!raw) return ''
  const normalized = raw.replace(/\/+$/, '')
  if (import.meta.env.DEV) {
    try {
      const u = new URL(normalized)
      const h = u.hostname.toLowerCase()
      if (h === 'localhost' || h === '127.0.0.1') {
        return ''
      }
    } catch {
      return normalized
    }
  }
  return normalized
}

const baseURL = resolveAxiosBaseURL()
if (baseURL) axios.defaults.baseURL = baseURL

axios.interceptors.request.use((config) => {
  config.headers = config.headers ?? {}
  config.headers['Accept-Language'] = locale.value === 'en' ? 'en' : 'ko'
  return config
})

/** 배포 환경에서 API 기준 URL (예: PDF 링크용) */
export function getApiBaseUrl(): string {
  return baseURL
}

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

/** NEO 등: 주척도별 그룹 (예: N (신경증) → [N1, N2, ...]) */
export interface ScaleGroupDto {
  groupLabel: string
  scaleCodes: string[]
}

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
  /** NEO 등: 주척도별 그룹. 있으면 척도별 점수/해석을 그룹 헤더와 함께 표시 */
  scaleGroups?: ScaleGroupDto[]
}

export async function fetchResult(resultId: string) {
  const res = await axios.get<ApiResponse<ResultViewData>>(`/api/results/${resultId}`)
  return res.data.data
}

/** 일반 사용자 검사 결과 PDF (axios로 요청해 Accept-Language·lang 쿼리 반영) */
export async function downloadUserResultPdf(resultId: string): Promise<void> {
  const res = await axios.get<Blob>(`/api/results/${resultId}/pdf`, {
    params: { lang: locale.value },
    responseType: 'blob',
  })
  const disposition = res.headers['content-disposition']
  let filename = locale.value === 'en' ? `result-${resultId}.pdf` : `검사결과-${resultId}.pdf`
  if (typeof disposition === 'string') {
    const match = disposition.match(/filename="?([^";\n]+)"?/)
    if (match) filename = match[1].trim()
  }
  const url = window.URL.createObjectURL(res.data)
  const a = document.createElement('a')
  a.href = url
  a.download = filename
  a.click()
  window.URL.revokeObjectURL(url)
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
  /** 척도 표시 순서 (결과 화면과 동일) */
  scaleOrder?: string[]
  /** 척도 코드 → 한글명 */
  scaleDisplayNames?: Record<string, string>
  /** NEO 등 주척도별 그룹 */
  scaleGroups?: ScaleGroupDto[]
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
  /** NEO 등 주척도별 그룹(기준점수 표시 N·E·O·A·C + N1~C6 순서) */
  scaleGroups?: ScaleGroupDto[]
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
  chartDailySubmissions?: string | null
  chartTScore?: string | null
}

function pdfDownloadTimestamp(): string {
  return new Date().toISOString().slice(0, 19).replace('T', '_').replace(/:/g, '-')
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
  const ts = pdfDownloadTimestamp()
  a.download =
    locale.value === 'en' ? `admin-report-summary-${ts}.pdf` : `관리자리포트-요약-${ts}.pdf`
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
  const ts = pdfDownloadTimestamp()
  a.download =
    locale.value === 'en' ? `admin-report-reference-${ts}.pdf` : `관리자리포트-기준해석-${ts}.pdf`
  a.click()
  window.URL.revokeObjectURL(url)
}

/** 관리자 - 선택한 응답의 결과 PDF (검사 완료 후 다운로드 화면과 동일) */
export interface BoardPostListItem {
  id: string
  title: string
  authorDisplay: string | null
  createdAt: string
  hasAdminReply: boolean
}

export interface BoardPostPage {
  content: BoardPostListItem[]
  totalElements: number
  totalPages: number
  number: number
  size: number
}

export interface BoardPostDetail {
  id: string
  title: string
  body: string
  authorDisplay: string | null
  createdAt: string
  adminReply: string | null
  adminRepliedAt: string | null
}

export interface BoardPostSummary {
  id: string
  title: string
  authorDisplay: string | null
  createdAt: string
  requiresPassword: boolean
}

export async function fetchBoardPosts(page = 0, size = 10): Promise<BoardPostPage> {
  const res = await axios.get<ApiResponse<BoardPostPage>>('/api/board/posts', {
    params: { page, size },
  })
  return res.data.data
}

export async function fetchBoardPostSummary(id: string): Promise<BoardPostSummary> {
  const res = await axios.get<ApiResponse<BoardPostSummary>>(`/api/board/posts/${id}/summary`)
  return res.data.data
}

export async function unlockBoardPost(id: string, password: string): Promise<BoardPostDetail> {
  const res = await axios.post<ApiResponse<BoardPostDetail>>(`/api/board/posts/${id}/unlock`, {
    password,
  })
  return res.data.data
}

export async function createBoardPost(payload: {
  title: string
  body: string
  password: string
  authorDisplay?: string | null
}): Promise<BoardPostDetail> {
  const res = await axios.post<ApiResponse<BoardPostDetail>>('/api/board/posts', payload)
  return res.data.data
}

export interface BoardAdminPostRow {
  id: string
  title: string
  authorDisplay: string | null
  createdAt: string
  hidden: boolean
  submitterIp: string
  hasAdminReply: boolean
}

export interface BoardAdminPostDetail {
  id: string
  title: string
  body: string
  authorDisplay: string | null
  createdAt: string
  hidden: boolean
  submitterIp: string
  adminReply: string | null
  adminRepliedAt: string | null
}

export interface BoardAdminPostPage {
  content: BoardAdminPostRow[]
  totalElements: number
  totalPages: number
  number: number
  size: number
}

export async function fetchAdminBoardPosts(token: string, page = 0, size = 20): Promise<BoardAdminPostPage> {
  const res = await axios.get<ApiResponse<BoardAdminPostPage>>('/api/admin/board/posts', {
    params: { page, size },
    headers: { 'X-Admin-Token': token },
  })
  return res.data.data
}

export async function patchAdminBoardPostHidden(token: string, id: string, hidden: boolean): Promise<void> {
  await axios.patch(
    `/api/admin/board/posts/${id}`,
    { hidden },
    { headers: { 'X-Admin-Token': token } },
  )
}

export async function deleteAdminBoardPost(token: string, id: string): Promise<void> {
  await axios.delete(`/api/admin/board/posts/${id}`, {
    headers: { 'X-Admin-Token': token },
  })
}

export async function fetchAdminBoardPostDetail(token: string, id: string): Promise<BoardAdminPostDetail> {
  const res = await axios.get<ApiResponse<BoardAdminPostDetail>>(`/api/admin/board/posts/${id}`, {
    headers: { 'X-Admin-Token': token },
  })
  return res.data.data
}

export async function patchAdminBoardPostReply(token: string, id: string, reply: string): Promise<void> {
  await axios.patch(`/api/admin/board/posts/${id}/reply`, { reply }, { headers: { 'X-Admin-Token': token } })
}

export async function downloadAdminResultPdf(token: string, resultId: string): Promise<void> {
  const res = await axios.get<Blob>(`/api/admin/results/${resultId}/pdf`, {
    headers: { 'X-Admin-Token': token },
    responseType: 'blob',
  })
  const disposition = res.headers['content-disposition']
  let filename = locale.value === 'en' ? `result-${resultId}.pdf` : `검사결과-${resultId}.pdf`
  if (typeof disposition === 'string') {
    const match = disposition.match(/filename="?([^";\n]+)"?/)
    if (match) filename = match[1].trim()
  }
  const url = window.URL.createObjectURL(res.data)
  const a = document.createElement('a')
  a.href = url
  a.download = filename
  a.click()
  window.URL.revokeObjectURL(url)
}

