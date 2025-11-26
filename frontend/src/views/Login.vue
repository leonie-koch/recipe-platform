<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { loginUser } from '@/client/recipe-client'
import type { LoginRequest } from '@/types/Auth'
import Card from 'primevue/card'
import InputText from 'primevue/inputtext'
import Password from 'primevue/password'
import Button from 'primevue/button'
import Message from 'primevue/message'

const router = useRouter()
const errorMessage = ref('')
const credentials = ref<LoginRequest>({
  username: '',
  password: '',
})

const handleLogin = async () => {
  errorMessage.value = ''

  if (!credentials.value.username || !credentials.value.password) {
    errorMessage.value = 'Bitte füllen Sie beide Felder aus.'
    return
  }

  try {
    // 1. API-Aufruf (Login)
    const response = await loginUser(credentials.value)

    // 2. Token speichern
    localStorage.setItem('jwt_token', response.data.token)

    // 3. Navigation zur Startseite
    router.push('/')
  } catch (error) {
    // 4. Fehlerbehandlung (z.B. 401 Unauthorized)
    console.error('Login fehlgeschlagen:', error)
    errorMessage.value = 'Anmeldung fehlgeschlagen. Überprüfen Sie Benutzername und Passwort.'
  }
}
</script>

<template>
  <div class="p-d-flex p-jc-center p-mt-6">
    <Card style="width: 25em">
      <template #title> Login </template>

      <template #content>
        <Message v-if="errorMessage" severity="error" :closable="false">
          {{ errorMessage }}
        </Message>

        <form @submit.prevent="handleLogin" class="p-fluid">
          <div class="p-field">
            <label for="username">Benutzername</label>
            <InputText
              id="username"
              type="text"
              v-model="credentials.username"
              aria-describedby="username-help"
              :class="{ 'p-invalid': errorMessage }"
              required
            />
          </div>

          <div class="p-field p-mt-3">
            <label for="password">Passwort</label>
            <Password
              id="password"
              v-model="credentials.password"
              :toggleMask="true"
              :feedback="false"
              :class="{ 'p-invalid': errorMessage }"
              required
            />
          </div>

          <Button type="submit" label="Anmelden" icon="pi pi-sign-in" class="p-mt-4" />
        </form>

        <p class="p-text-center p-mt-3">
          Noch kein Konto? <router-link to="/register">Hier registrieren</router-link>
        </p>
      </template>
    </Card>
  </div>
</template>
