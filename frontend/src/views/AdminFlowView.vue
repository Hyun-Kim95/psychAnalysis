<template>
  <section class="admin-flow pa-admin-flow">
    <AdminLoginView v-if="!adminToken" @logged-in="handleAdminLoggedIn" />
    <AdminDashboardView v-else :token="adminToken" />
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import AdminLoginView from './AdminLoginView.vue'
import AdminDashboardView from './AdminDashboardView.vue'

const adminToken = ref<string | null>(null)

function handleAdminLoggedIn(payload: { token: string }) {
  adminToken.value = payload.token
}
</script>
