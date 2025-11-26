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

const router = useRouter()
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
    // 1. API-Aufruf (Registrierung)
    const response = await registerUser(credentials.value)

    // 2. Token speichern (Login direkt nach Registrierung)
    localStorage.setItem('jwt_token', response.data.token)

    // 3. Optional: Erfolgsmeldung anzeigen und navigieren
    successMessage.value = 'Registrierung erfolgreich! Sie werden weitergeleitet...'

    // 4. Navigation nach kurzer Verzögerung
    setTimeout(() => {
      router.push('/')
    }, 1000)
  } catch (error) {
    console.error('Registrierung fehlgeschlagen:', error)

    // Spezifische Behandlung für den 409 Conflict (Username Taken) aus dem Backend
    if (error.response && error.response.status === 409) {
      errorMessage.value = 'Dieser Benutzername ist bereits vergeben.'
    } else {
      errorMessage.value = 'Registrierung fehlgeschlagen. Bitte versuchen Sie es erneut.'
    }
  }
}
</script>

<template>
  <div class="p-d-flex p-jc-center p-mt-6">
    <Card style="width: 25em">
      <template #title> Konto erstellen </template>

      <template #content>
        <Message v-if="successMessage" severity="success" :closable="false">{{
          successMessage
        }}</Message>
        <Message v-if="errorMessage" severity="error" :closable="false">{{ errorMessage }}</Message>

        <form @submit.prevent="handleRegister" class="p-fluid">
          <div class="p-field">
            <label for="username">Benutzername</label>
            <InputText id="username" type="text" v-model="credentials.username" required />
          </div>

          <div class="p-field p-mt-3">
            <label for="password">Passwort</label>
            <Password
              id="password"
              v-model="credentials.password"
              :toggleMask="true"
              :feedback="false"
              required
            />
          </div>

          <Button type="submit" label="Registrieren" icon="pi pi-user-plus" class="p-mt-4" />
        </form>

        <p class="p-text-center p-mt-3">
          Bereits registriert? <router-link to="/login">Hier anmelden</router-link>
        </p>
      </template>
    </Card>
  </div>
</template>
