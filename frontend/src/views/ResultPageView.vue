<template>
  <ResultView :result-id="resultId" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import ResultView from './ResultView.vue'

const route = useRoute()
const resultId = computed(() => (route.params.resultId as string) ?? null)
</script>
