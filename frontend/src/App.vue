<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, RouterView } from 'vue-router'
import Menubar from 'primevue/menubar'
import Button from 'primevue/button'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const handleLogout = () => {
  authStore.setToken(null)
  router.push('/login')
}

const menuItems = computed(() => {
  const items = [{ label: 'Home Feed', icon: 'pi pi-home', command: () => router.push('/') }]

  return items
})
</script>

<template>
  <div id="layout" class="flex flex-col min-h-screen">
    <Menubar :model="menuItems">
      <template #start>
        <div class="font-bold text-lg cursor-pointer mr-4" @click="router.push('/')">
          Recipe Platform
        </div>
      </template>
      <template #end>
        <div class="flex items-center space-x-2">
          <template v-if="!authStore.isLoggedIn">
            <Button
              label="Login"
              icon="pi pi-sign-in"
              severity="primary"
              text
              @click="router.push('/login')"
            />
            <Button
              label="Registrieren"
              icon="pi pi-user-plus"
              severity="success"
              @click="router.push('/register')"
            />
          </template>

          <template v-else>
            <Button label="Logout" icon="pi pi-sign-out" severity="danger" @click="handleLogout" />
          </template>
        </div>
      </template>
    </Menubar>

    <main id="content" class="flex-grow w-full">
      <RouterView />
    </main>
  </div>
</template>
