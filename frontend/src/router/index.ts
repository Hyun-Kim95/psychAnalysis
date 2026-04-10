import { createRouter, createWebHistory } from 'vue-router'
import UserFlowView from '../views/UserFlowView.vue'
import ResultPageView from '../views/ResultPageView.vue'
import AdminFlowView from '../views/AdminFlowView.vue'

export const router = createRouter({
  history: createWebHistory(),
  scrollBehavior(to) {
    if (to.hash) {
      return { el: to.hash, behavior: 'smooth', top: 72 }
    }
    return { top: 0 }
  },
  routes: [
    { path: '/', name: 'user', component: UserFlowView },
    { path: '/result/:resultId', name: 'result', component: ResultPageView },
    { path: '/admin', name: 'admin', component: AdminFlowView },
  ],
})
