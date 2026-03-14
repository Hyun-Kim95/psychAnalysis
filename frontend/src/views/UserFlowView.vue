<template>
  <div>
    <AssessmentListView v-if="step === 'list'" @select="handleSelectAssessment" />
    <IntroView
      v-else-if="step === 'intro'"
      :assessment-id="selectedAssessmentId!"
      @start="handleStart"
      @back="step = 'list'"
    />
    <TestView
      v-else-if="step === 'test'"
      :assessment-id="assessmentId!"
      :session-id="sessionId!"
      @completed="handleCompleted"
      @cancel="handleCancel"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import AssessmentListView from './AssessmentListView.vue'
import IntroView from './IntroView.vue'
import TestView from './TestView.vue'
import type { AssessmentSummary } from '../api'

type Step = 'list' | 'intro' | 'test'

const router = useRouter()
const step = ref<Step>('list')
const selectedAssessmentId = ref<number | null>(null)
const assessmentId = ref<number | null>(null)
const sessionId = ref<string | null>(null)

function handleSelectAssessment(assessment: AssessmentSummary) {
  selectedAssessmentId.value = assessment.id
  step.value = 'intro'
}

function handleStart(payload: { assessmentId: number; sessionId: string }) {
  assessmentId.value = payload.assessmentId
  sessionId.value = payload.sessionId
  step.value = 'test'
}

function handleCompleted(payload: { resultId: string }) {
  router.push(`/result/${payload.resultId}`)
}

function handleCancel() {
  step.value = 'intro'
}
</script>
