/** Public site URLs from Vite env (see `frontend/.env.example`). */

function trimSlash(s: string): string {
  return s.replace(/\/+$/, '')
}

const originRaw = import.meta.env.VITE_SITE_ORIGIN?.trim() ?? ''
export const siteOrigin = originRaw ? trimSlash(originRaw) : ''

export const contactFormUrl = (import.meta.env.VITE_CONTACT_FORM_URL ?? '').trim()
export const contactEmail = (import.meta.env.VITE_CONTACT_EMAIL ?? '').trim()
export const donationUrl = (import.meta.env.VITE_DONATION_URL ?? '').trim()

export function contactMailtoHref(email: string): string {
  const e = email.trim()
  if (!e) return ''
  return `mailto:${e}`
}
