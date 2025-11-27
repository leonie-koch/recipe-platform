<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getRecipes, deleteRecipe } from '@/client/recipe-client'
import RecipeCard from '@/components/RecipeCard.vue'
import RecipeDetail from '@/components/RecipeDetail.vue'
import Dialog from 'primevue/dialog'
import type { Recipe } from '@/types/Recipe'

import ConfirmDialog from 'primevue/confirmdialog'
import { useConfirm } from 'primevue/useconfirm'
import { useToast } from 'primevue/usetoast'
import Toast from 'primevue/toast'
import ProgressSpinner from 'primevue/progressspinner'
import Button from 'primevue/button'
import Message from 'primevue/message'
import { RouterLink } from 'vue-router'

const confirm = useConfirm()
const toast = useToast()

const recipes = ref<Recipe[]>([])
const isLoading = ref(true)
const error = ref<string | null>(null)

const isDetailVisible = ref(false)
const selectedRecipeId = ref<number | null>(null)
const selectedRecipeTitle = ref<string>('Rezept-Details')

const openRecipeDetail = (recipe: Recipe) => {
  selectedRecipeId.value = recipe.id
  selectedRecipeTitle.value = recipe.title
  isDetailVisible.value = true
}

const closeRecipeDetail = () => {
  isDetailVisible.value = false
  selectedRecipeId.value = null
}

const confirmDeletion = () => {
  if (!selectedRecipeId.value) return

  confirm.require({
    message: `Sind Sie sicher, dass Sie das Rezept "${selectedRecipeTitle.value}" löschen möchten?`,
    header: 'Rezept löschen',
    icon: 'pi pi-info-circle',
    rejectLabel: 'Abbrechen',
    acceptLabel: 'Löschen',
    rejectClass: 'p-button-secondary p-button-outlined',
    acceptClass: 'p-button-danger',
    accept: async () => handleDeletion(selectedRecipeId.value!),
  })
}

const handleDeletion = async (id: number) => {
  try {
    isLoading.value = true
    closeRecipeDetail()

    await deleteRecipe(id)
    await loadRecipes()

    toast.add({
      severity: 'success',
      summary: 'Erfolg',
      detail: 'Rezept erfolgreich gelöscht!',
    })
  } catch {
    toast.add({
      severity: 'error',
      summary: 'Fehler',
      detail: 'Rezept konnte nicht gelöscht werden.',
    })
  } finally {
    isLoading.value = false
  }
}

const loadRecipes = async () => {
  error.value = null
  try {
    const response = await getRecipes()
    recipes.value = response.data
  } catch {
    error.value = 'Fehler beim Laden der Rezepte. Läuft das Backend?'
  }
}

onMounted(async () => {
  await loadRecipes()
  isLoading.value = false
})
</script>

<template>
  <Toast />
  <ConfirmDialog />

  <div class="w-full max-w-6xl mx-auto px-4 py-10">
    <div class="flex items-center justify-between mb-8">
      <h1 class="text-3xl font-bold">Rezept-Feed 🍳</h1>

      <RouterLink to="/recipes/new">
        <Button label="Neues Rezept erstellen" icon="pi pi-plus" severity="success" />
      </RouterLink>
    </div>

    <div v-if="isLoading" class="flex items-center justify-center gap-4 py-10">
      <ProgressSpinner />
      <p class="text-lg">Rezepte werden geladen...</p>
    </div>

    <div v-else-if="error">
      <Message severity="error" :closable="false">{{ error }}</Message>
    </div>

    <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      <div
        v-for="recipe in recipes"
        :key="recipe.id"
        @click="openRecipeDetail(recipe)"
        class="cursor-pointer"
      >
        <RecipeCard :recipe="recipe" />
      </div>

      <div v-if="recipes.length === 0" class="col-span-full">
        <Message severity="info" :closable="false">
          Es wurden noch keine Rezepte gefunden.
        </Message>
      </div>
    </div>

    <Dialog
      v-model:visible="isDetailVisible"
      modal
      :style="{ width: '75vw' }"
      :draggable="false"
      :maximizable="true"
      @update:visible="closeRecipeDetail"
    >
      <template #header>
        <div class="flex items-center justify-between w-full">
          <h3 class="text-xl font-semibold">{{ selectedRecipeTitle }}</h3>
          <Button icon="pi pi-trash" severity="danger" text rounded @click.stop="confirmDeletion" />
        </div>
      </template>

      <RecipeDetail :recipe-id="selectedRecipeId" />
    </Dialog>
  </div>
</template>
