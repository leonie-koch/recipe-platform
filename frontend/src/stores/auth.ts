import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

export const useAuthStore = defineStore('counter', () => {
  const token = ref(localStorage.getItem('jwt_token'))
  const isLoggedIn = computed(() => !!token.value)
  function setToken(newToken: string | null) {
    if (newToken) {
      localStorage.setItem('jwt_token', newToken)
      token.value = newToken
    } else {
      localStorage.removeItem('jwt_token')
      token.value = null
    }
  }

  return { token, isLoggedIn, setToken }
})
