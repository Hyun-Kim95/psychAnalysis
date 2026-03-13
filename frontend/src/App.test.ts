import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeAll } from 'vitest'
import App from './App.vue'

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
    const wrapper = mount(App)
    expect(wrapper.text()).toContain('심리검사 프로토타입')
  })
})

