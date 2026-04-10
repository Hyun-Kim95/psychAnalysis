import type { Locale } from './i18n'

/** 관리자 대시보드 차트 등: 척도 코드 → 표시용 이름 (신뢰도 α 그래프용) */
const SCALE_LABEL_EN: Record<string, string> = {
  ER: 'Emotion regulation',
  IC: 'Impulse control',
  CA: 'Causal analysis',
  CO: 'Communication',
  EM: 'Empathy',
  SE: 'Relatedness / support',
  OP: 'Optimism / control',
  LS: 'Life satisfaction',
  GR: 'Gratitude',
  NS: 'Novelty seeking',
  HA: 'Harm avoidance',
  RD: 'Reward dependence',
  P: 'Persistence',
  SD: 'Self-directedness',
  C: 'Cooperativeness',
  ST: 'Self-transcendence',
  N: 'Neuroticism',
  E: 'Extraversion',
  O: 'Openness',
  A: 'Agreeableness',
  D: 'Depression',
  R: 'Resilience',
}

/** KRQ 하위척도 한글 (DB scale.name 과 동일) */
const SCALE_LABEL_KO_KRQ: Record<string, string> = {
  ER: '감정조절력',
  IC: '충동통제력',
  CA: '원인분석력',
  CO: '소통능력',
  EM: '공감능력',
  SE: '자아확장력',
  OP: '자아낙관성',
  LS: '생활만족도',
  GR: '감사하는 태도',
}

const RELIABILITY_KEY_SEP = '\u001e'

/** 척도 코드만 또는 관리자 신뢰도 키 `검사명\u001e척도코드` */
export function formatScaleCodeForChart(code: string, loc: Locale): string {
  const sep = code.indexOf(RELIABILITY_KEY_SEP)
  if (sep !== -1) {
    const assessment = code.slice(0, sep)
    const scaleCode = code.slice(sep + RELIABILITY_KEY_SEP.length)
    const scaleLabel = formatScaleCodeForChart(scaleCode, loc)
    return `${assessment} · ${scaleLabel}`
  }
  if (loc === 'en') {
    return SCALE_LABEL_EN[code] ?? code
  }
  return SCALE_LABEL_KO_KRQ[code] ?? code
}
