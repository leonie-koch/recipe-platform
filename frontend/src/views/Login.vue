<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { loginUser } from '@/client/recipe-client'
import Card from 'primevue/card'
import InputText from 'primevue/inputtext'
import Password from 'primevue/password'
import Button from 'primevue/button'
import Message from 'primevue/message'
import type { LoginRequest } from '@/types/Auth'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const errorMessage = ref('')
const credentials = ref<LoginRequest>({ username: '', password: '' })

const handleLogin = async () => {
  errorMessage.value = ''

  if (!credentials.value.username || !credentials.value.password) {
    errorMessage.value = 'Bitte füllen Sie beide Felder aus.'
    return
  }

  try {
    const response = await loginUser(credentials.value)
    authStore.setToken(response.data.token)
    router.push('/')
  } catch (error) {
    errorMessage.value = 'Anmeldung fehlgeschlagen. Überprüfen Sie Benutzername und Passwort.'
  }
}
</script>

<template>
  <div class="flex items-center justify-center min-h-screen bg-gray-50">
    <Card class="w-full max-w-md shadow-lg p-8">
      <template #title>
        <h2 class="text-2xl font-bold text-center">Login</h2>
      </template>

      <template #content>
        <Message v-if="errorMessage" severity="error" :closable="false" class="mb-4">
          {{ errorMessage }}
        </Message>

        <form @submit.prevent="handleLogin" class="space-y-4">
          <div>
            <label for="username" class="block mb-1 text-sm font-medium text-gray-700">
              Benutzername
            </label>
            <InputText
              id="username"
              v-model="credentials.username"
              class="w-full border border-gray-300 rounded-md p-2"
            />
          </div>

          <div>
            <label for="password" class="block mb-1 text-sm font-medium text-gray-700">
              Passwort
            </label>
            <Password
              id="password"
              v-model="credentials.password"
              :feedback="false"
              :toggleMask="true"
              class="w-full border border-gray-300 rounded-md p-2"
            />
          </div>

          <Button type="submit" label="Anmelden" icon="pi pi-sign-in" class="w-full mt-2" />
        </form>

        <p class="mt-4 text-center text-sm text-gray-600">
          Noch kein Konto?
          <router-link to="/register" class="text-blue-600 hover:text-blue-800 font-medium">
            Jetzt registrieren
          </router-link>
        </p>
      </template>
    </Card>
  </div>
</template>

<style>
.login-container {
  width: 100%;
  max-width: 400px;
}

.login-card {
  width: 100%;
}

.input {
  width: 100%;
  margin-bottom: 15px;
}

.register {
  margin-top: 15px;
  text-align: center;
}
</style>
