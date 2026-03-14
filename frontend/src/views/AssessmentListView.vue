<template>
  <section class="assessment-list">
    <div v-if="loading" class="card">
      <p>검사 목록을 불러오는 중입니다...</p>
    </div>

    <div v-else-if="error" class="card card-error">
      <p>{{ error }}</p>
    </div>

    <div v-else class="card">
      <p class="intro-desc">진행할 검사와 버전(간단/상세)을 선택해 주세요.</p>

      <ul class="assessment-cards">
        <li
          v-for="group in assessmentGroups"
          :key="group.baseName"
          class="assessment-card assessment-card--group"
        >
          <h3 class="assessment-card-title">{{ group.baseName }}</h3>
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
              {{ v.isDetail ? '상세 검사' : '간단 검사' }}{{ v.itemCount ? ` (${v.itemCount}문항)` : '' }}
            </button>
          </div>
        </li>
      </ul>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { fetchAssessments, type AssessmentSummary } from '../api'

const emit = defineEmits<{
  (e: 'select', assessment: AssessmentSummary): void
}>()

const assessments = ref<AssessmentSummary[]>([])
const loading = ref(true)
const error = ref<string | null>(null)

const assessmentGroups = computed(() => {
  const list = assessments.value
  const map = new Map<string, { baseName: string; description: string; versions: { assessment: AssessmentSummary; itemCount: number; isDetail: boolean }[] }>()
  for (const a of list) {
    const isDetail = a.name.endsWith(' (상세)')
    const baseName = isDetail ? a.name.replace(/ \(상세\)$/, '') : a.name
    if (!map.has(baseName)) {
      map.set(baseName, { baseName, description: '', versions: [] })
    }
    const g = map.get(baseName)!
    if (!g.description && a.description) g.description = a.description.replace(/\s*상세 버전.*$/, '').replace(/\s*간단 버전.*$/, '').trim()
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

onMounted(async () => {
  try {
    assessments.value = await fetchAssessments()
  } catch (e) {
    error.value = '검사 목록을 불러오지 못했습니다.'
  } finally {
    loading.value = false
  }
})

function select(a: AssessmentSummary) {
  emit('select', a)
}
</script>
