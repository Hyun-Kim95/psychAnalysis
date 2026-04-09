<template>
  <section class="assessment-list">
    <div v-if="loading" class="card">
      <p>{{ t('loadingAssessmentList') }}</p>
    </div>

    <div v-else-if="error" class="card card-error">
      <p>{{ error }}</p>
    </div>

    <div v-else class="card">
      <p class="intro-desc">{{ t('listChooseVersion') }}</p>

      <ul class="assessment-cards">
        <li
          v-for="group in assessmentGroups"
          :key="group.baseName"
          class="assessment-card assessment-card--group"
        >
          <h3 class="assessment-card-title">{{ group.displayName }}</h3>
          <p v-if="group.description" class="assessment-card-desc">{{ group.description }}</p>
          <div class="assessment-card-actions">
            <button
              v-for="v in group.versions"
              :key="v.assessment.id"
              type="button"
              class="version-btn"
              :class="v.isDetail ? 'version-btn--detail' : 'version-btn--short'"
              @click="select(v.assessment)"
            >
              {{ v.isDetail ? t('detailVersion') : t('shortVersion') }}{{ v.itemCount ? ` (${t('itemCount', { count: v.itemCount })})` : '' }}
            </button>
          </div>
        </li>
      </ul>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { fetchAssessments, type AssessmentSummary } from '../api'
import { useI18n } from '../i18n'

const emit = defineEmits<{
  (e: 'select', assessment: AssessmentSummary): void
}>()

const assessments = ref<AssessmentSummary[]>([])
const loading = ref(true)
const error = ref<string | null>(null)
const { t, locale } = useI18n()

const DISPLAY_DESCRIPTIONS_KO: Record<string, string> = {
  'BDI 우울검사': 'Beck Depression Inventory(벡 우울척도)',
  'BDI 우울검사 (상세)': 'Beck Depression Inventory(벡 우울척도, 21문항)',
  'BAI 불안검사': 'Beck Anxiety Inventory(벡 불안척도)',
  'BAI 불안검사 (상세)': 'Beck Anxiety Inventory(벡 불안척도, 21문항)',
  'TCI 검사': '기질·성격요인(TCI)',
  'TCI 검사 (상세)': '기질·성격요인(TCI, 상세)',
  '회복탄력성 검사': '한국인 회복탄력성 척도(KRQ)',
  '회복탄력성 검사 (상세)': '한국인 회복탄력성 척도(KRQ-53)',
  'NEO 성격검사': 'NEO 네오 성격검사(대학·성인용). 29개 하위척도, 29문항. 약 7~10분.',
  'NEO 성격검사 (상세)': 'NEO 성격검사 (성인용, 58문항)',
}

const DISPLAY_DESCRIPTIONS_EN: Record<string, string> = {
  'BDI 우울검사': 'Beck Depression Inventory (BDI)',
  'BDI 우울검사 (상세)': 'Beck Depression Inventory (BDI, 21 items)',
  'BDI Depression Test': 'Beck Depression Inventory (BDI)',
  'BDI Depression Test (Detailed)': 'Beck Depression Inventory (BDI, 21 items)',
  'BAI 불안검사': 'Beck Anxiety Inventory (BAI)',
  'BAI 불안검사 (상세)': 'Beck Anxiety Inventory (BAI, 21 items)',
  'BAI Anxiety Test': 'Beck Anxiety Inventory (BAI)',
  'BAI Anxiety Test (Detailed)': 'Beck Anxiety Inventory (BAI, 21 items)',
  'TCI 검사': 'Temperament and Character Inventory (TCI)',
  'TCI 검사 (상세)': 'Temperament and Character Inventory (TCI, detailed)',
  'TCI Temperament & Character Inventory': 'Temperament and Character Inventory (TCI)',
  'TCI Temperament & Character Inventory (Detailed)': 'Temperament and Character Inventory (TCI, detailed)',
  '회복탄력성 검사': 'Korean Resilience Quotient (KRQ)',
  '회복탄력성 검사 (상세)': 'Korean Resilience Quotient (KRQ-53)',
  'Resilience Test': 'Korean Resilience Quotient (KRQ)',
  'Resilience Test (Detailed)': 'Korean Resilience Quotient (KRQ-53)',
  'NEO 성격검사': 'NEO personality inventory for adults/college. 29 facets, 29 items. About 7–10 min.',
  'NEO 성격검사 (상세)': 'NEO Personality Inventory—Revised (adult). 58 items. About 10–15 min.',
  'NEO Personality Test': 'NEO personality inventory for adults/college. 29 facets, 29 items. About 7–10 min.',
  'NEO Personality Test (Detailed)': 'NEO Personality Inventory—Revised (adult). 58 items. About 10–15 min.',
}

const DISPLAY_NAMES_EN: Record<string, string> = {
  'BDI 우울검사': 'BDI Depression Test',
  'BAI 불안검사': 'BAI Anxiety Test',
  'TCI 검사': 'TCI Inventory',
  '회복탄력성 검사': 'Resilience Test',
  'NEO 성격검사': 'NEO Personality Test',
}

const assessmentGroups = computed(() => {
  const list = assessments.value
  const map = new Map<string, { baseName: string; displayName: string; description: string; versions: { assessment: AssessmentSummary; itemCount: number; isDetail: boolean }[] }>()
  for (const a of list) {
    const isDetail = a.name.endsWith(' (상세)') || a.name.endsWith(' (Detailed)')
    const baseName = isDetail ? a.name.replace(/ \((상세|Detailed)\)$/, '') : a.name
    if (!map.has(baseName)) {
      map.set(baseName, {
        baseName,
        displayName: locale.value === 'en' ? (DISPLAY_NAMES_EN[baseName] ?? baseName) : baseName,
        description: '',
        versions: [],
      })
    }
    const g = map.get(baseName)!
    if (!g.description) {
      const display =
        locale.value === 'en'
          ? DISPLAY_DESCRIPTIONS_EN[a.name]
          : DISPLAY_DESCRIPTIONS_KO[a.name]
      g.description = display
        || a.description?.replace(/\s*상세 버전.*$/, '').replace(/\s*간단 버전.*$/, '').replace(/\s*Detailed version.*$/i, '').replace(/\s*Short version.*$/i, '').trim()
        || ''
    }
    g.versions.push({
      assessment: a,
      itemCount: a.itemCount ?? 0,
      isDetail,
    })
  }
  for (const g of map.values()) {
    g.versions.sort((a, b) => (a.isDetail ? 1 : 0) - (b.isDetail ? 1 : 0))
  }
  return Array.from(map.values())
})

async function loadAssessments() {
  try {
    assessments.value = await fetchAssessments()
  } catch (e) {
    error.value = t('assessmentListLoadError')
  } finally {
    loading.value = false
  }
}

onMounted(loadAssessments)
watch(locale, loadAssessments)

function select(a: AssessmentSummary) {
  emit('select', a)
}
</script>
