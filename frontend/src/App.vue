<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRouter, RouterView } from 'vue-router'
import Menubar from 'primevue/menubar'
import Button from 'primevue/button'

const router = useRouter()

const isLoggedIn = computed(() => !!localStorage.getItem('jwt_token'))

const handleLogout = () => {
  localStorage.removeItem('jwt_token')
  router.push('/login')
}

const menuItems = ref([
  {
    label: 'Home Feed',
    icon: 'pi pi-home',
    command: () => router.push('/'),
  },
])
</script>

<template>
  <div id="layout" class="flex flex-col min-h-screen">
    <Menubar :model="menuItems" class="w-full">
      <template #start>
        <div
          class="font-bold text-lg cursor-pointer text-blue-600 hover:text-blue-700"
          @click="router.push('/')"
        >
          Recipe Platform
        </div>
      </template>

      <template #end>
        <div class="flex items-center space-x-2">
          <template v-if="!isLoggedIn">
            <Button
              label="Login"
              icon="pi pi-sign-in"
              severity="secondary"
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
            <Button
              label="Neues Rezept"
              icon="pi pi-plus"
              severity="info"
              text
              @click="router.push('/recipes/new')"
            />
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
