import { ref } from 'vue'
import type { RouteLocationNormalizedLoaded } from 'vue-router'
import { t } from '../i18n'

const lastRoute = ref<RouteLocationNormalizedLoaded | null>(null)

function upsertMetaName(name: string, content: string) {
  if (typeof document === 'undefined') return
  let el = document.querySelector(`meta[name="${name}"]`) as HTMLMetaElement | null
  if (!el) {
    el = document.createElement('meta')
    el.setAttribute('name', name)
    document.head.appendChild(el)
  }
  el.setAttribute('content', content)
}

function applyFromRoute(to: RouteLocationNormalizedLoaded) {
  if (typeof document === 'undefined') return
  if (to.path === '/admin') return

  const titleKey = to.meta.titleKey as string | undefined
  const descriptionKey = to.meta.descriptionKey as string | undefined
  const robots = to.meta.robots as string | undefined

  if (titleKey) {
    document.title = t(titleKey)
  }
  if (descriptionKey) {
    upsertMetaName('description', t(descriptionKey))
  }
  if (robots) {
    upsertMetaName('robots', robots)
  }
}

export function setLastRouteForSeo(to: RouteLocationNormalizedLoaded) {
  lastRoute.value = to
  applyFromRoute(to)
}

export function refreshSeoFromLastRoute() {
  if (lastRoute.value) applyFromRoute(lastRoute.value)
}
