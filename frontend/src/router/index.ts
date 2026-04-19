import { createRouter, createWebHistory } from 'vue-router'
import UserFlowView from '../views/UserFlowView.vue'
import ResultPageView from '../views/ResultPageView.vue'
import AdminFlowView from '../views/AdminFlowView.vue'
import InfoView from '../views/InfoView.vue'
import BoardListView from '../views/BoardListView.vue'
import BoardNewView from '../views/BoardNewView.vue'
import BoardDetailView from '../views/BoardDetailView.vue'
import { setLastRouteForSeo } from '../seo/documentSeo'

export const router = createRouter({
  history: createWebHistory(),
  scrollBehavior(to) {
    if (to.hash) {
      return { el: to.hash, behavior: 'smooth', top: 72 }
    }
    return { top: 0 }
  },
  routes: [
    {
      path: '/',
      name: 'user',
      component: UserFlowView,
      meta: {
        titleKey: 'metaTitleHome',
        descriptionKey: 'metaDescHome',
        robots: 'index, follow',
      },
    },
    {
      path: '/info',
      name: 'info',
      component: InfoView,
      meta: {
        titleKey: 'metaTitleInfo',
        descriptionKey: 'metaDescInfo',
        robots: 'index, follow',
      },
    },
    {
      path: '/board/new',
      name: 'board-new',
      component: BoardNewView,
      meta: {
        titleKey: 'metaTitleBoardNew',
        descriptionKey: 'metaDescBoardNew',
        robots: 'index, follow',
      },
    },
    {
      path: '/board/:id',
      name: 'board-detail',
      component: BoardDetailView,
      meta: {
        titleKey: 'metaTitleBoardDetail',
        descriptionKey: 'metaDescBoardDetail',
        robots: 'index, follow',
      },
    },
    {
      path: '/board',
      name: 'board-list',
      component: BoardListView,
      meta: {
        titleKey: 'metaTitleBoard',
        descriptionKey: 'metaDescBoard',
        robots: 'index, follow',
      },
    },
    {
      path: '/result/:resultId',
      name: 'result',
      component: ResultPageView,
      meta: {
        titleKey: 'metaTitleResult',
        descriptionKey: 'metaDescResult',
        robots: 'noindex, nofollow',
      },
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminFlowView,
    },
  ],
})

const GA_MEASUREMENT_ID = 'G-B7J46GVHPG'

router.afterEach((to) => {
  setLastRouteForSeo(to)
  const gtag = (window as Window & { gtag?: (...args: unknown[]) => void }).gtag
  gtag?.('config', GA_MEASUREMENT_ID, {
    page_path: to.fullPath,
  })
})
