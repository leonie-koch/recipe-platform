<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { registerUser } from '@/client/recipe-client'
import type { RegisterRequest } from '@/types/Auth'

import Card from 'primevue/card'
import InputText from 'primevue/inputtext'
import Password from 'primevue/password'
import Button from 'primevue/button'
import Message from 'primevue/message'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const errorMessage = ref('')
const successMessage = ref('')
const credentials = ref<RegisterRequest>({
  username: '',
  password: '',
})

const handleRegister = async () => {
  errorMessage.value = ''
  successMessage.value = ''

  if (credentials.value.username.length < 3 || credentials.value.password.length < 6) {
    errorMessage.value = 'Benutzername muss mind. 3 Zeichen und Passwort mind. 6 Zeichen lang sein.'
    return
  }

  try {
    const response = await registerUser(credentials.value)
    authStore.setToken(response.data.token)
    successMessage.value = 'Registrierung erfolgreich! Sie werden weitergeleitet...'

    setTimeout(() => {
      router.push('/')
    }, 1000)
  } catch (error: any) {
    console.error('Registrierung fehlgeschlagen:', error)
    if (error.response && error.response.status === 409) {
      errorMessage.value = 'Dieser Benutzername ist bereits vergeben.'
    } else {
      errorMessage.value = 'Registrierung fehlgeschlagen. Bitte versuchen Sie es erneut.'
    }
  }
}
</script>

<template>
  <div class="flex items-center justify-center min-h-screen bg-gray-50">
    <Card class="w-full max-w-md shadow-lg p-8">
      <template #title>
        <h2 class="text-2xl font-bold text-center">Konto erstellen</h2>
      </template>

      <template #content>
        <div class="space-y-3">
          <Message v-if="successMessage" severity="success" :closable="false" class="mb-2">
            {{ successMessage }}
          </Message>
          <Message v-if="errorMessage" severity="error" :closable="false" class="mb-2">
            {{ errorMessage }}
          </Message>
        </div>

        <form @submit.prevent="handleRegister" class="space-y-4">
          <div>
            <label for="username" class="block mb-1 text-sm font-medium text-gray-700">
              Benutzername
            </label>
            <InputText
              id="username"
              type="text"
              v-model="credentials.username"
              class="w-full border border-gray-300 rounded-md p-2"
              required
            />
          </div>

          <div>
            <label for="password" class="block mb-1 text-sm font-medium text-gray-700">
              Passwort
            </label>
            <Password
              id="password"
              v-model="credentials.password"
              :toggleMask="true"
              :feedback="false"
              class="w-full border border-gray-300 rounded-md p-2"
              required
            />
          </div>

          <Button type="submit" label="Registrieren" icon="pi pi-user-plus" class="w-full mt-2" />
        </form>

        <p class="mt-4 text-center text-sm text-gray-600">
          Bereits registriert?
          <router-link to="/login" class="text-blue-600 hover:text-blue-800 font-medium">
            Hier anmelden
          </router-link>
        </p>
      </template>
    </Card>
  </div>
</template>
