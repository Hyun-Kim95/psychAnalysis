<template>
  <div id="app-root">
    <header class="app-header">
      <h1 class="app-title">심리 검사</h1>
      <p class="app-subtitle">온라인 심리 검사 서비스</p>
    </header>

    <nav v-if="isAdminRoute" class="app-nav">
      <router-link to="/" class="nav-btn">검사 홈으로</router-link>
    </nav>

    <main class="app-main">
      <router-view />
    </main>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const isAdminRoute = computed(() => route.path === '/admin')
</script>
