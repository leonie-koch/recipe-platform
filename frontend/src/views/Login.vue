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

const router = useRouter()
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
    localStorage.setItem('jwt_token', response.data.token)
    router.push('/')
  } catch (error) {
    errorMessage.value = 'Anmeldung fehlgeschlagen. Überprüfen Sie Benutzername und Passwort.'
  }
}
</script>

<template>
  <div class="h-full w-full flex items-center justify-center p-4">
    <Card style="width: 25em" class="shadow-lg max-w-sm w-full">
      <template #title>Login</template>

      <template #content>
        <Message v-if="errorMessage" severity="error" :closable="false" class="mb-4">
          {{ errorMessage }}
        </Message>

        <form @submit.prevent="handleLogin" class="space-y-4">
          <div>
            <label for="username" class="block mb-1 text-sm font-medium text-gray-700"
              >Benutzername</label
            >
            <InputText id="username" v-model="credentials.username" class="w-full" />
          </div>

          <div>
            <label for="password" class="block mb-1 text-sm font-medium text-gray-700"
              >Passwort</label
            >
            <Password
              id="password"
              v-model="credentials.password"
              :feedback="false"
              :toggleMask="true"
              class="w-full"
            />
          </div>

          <Button type="submit" label="Anmelden" icon="pi pi-sign-in" class="w-full mt-4" />
        </form>

        <p class="mt-4 text-center text-sm">
          Noch kein Konto?
          <router-link to="/register" class="text-blue-600 hover:text-blue-800 font-medium"
            >Jetzt registrieren</router-link
          >
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
