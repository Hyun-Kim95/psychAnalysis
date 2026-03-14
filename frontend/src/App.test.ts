import { mount } from '@vue/test-utils'
import { describe, it, expect, vi } from 'vitest'
import App from './App.vue'
import { router } from './router'

vi.mock('axios', () => {
  return {
    default: {
      get: vi.fn(() =>
        Promise.resolve({
          data: { success: true, data: {} },
        }),
      ),
      post: vi.fn(() =>
        Promise.resolve({
          data: { success: true, data: { token: 'dummy', responseSessionId: 'session', resultId: 'result' } },
        }),
      ),
    },
  }
})

describe('App', () => {
  it('renders main title', () => {
    const wrapper = mount(App, {
      global: {
        plugins: [router],
      },
    })
    expect(wrapper.text()).toContain('심리 검사')
  })
})

