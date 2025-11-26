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

const confirm = useConfirm()
const toast = useToast()

import ProgressSpinner from 'primevue/progressspinner'
import Button from 'primevue/button'
import Message from 'primevue/message'

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
  selectedRecipeTitle.value = 'Rezept-Details'
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
    accept: async () => {
      await handleDeletion(selectedRecipeId.value!)
    },
    reject: () => {
      toast.add({
        severity: 'info',
        summary: 'Abgebrochen',
        detail: 'Löschvorgang abgebrochen.',
        life: 3000,
      })
    },
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
      detail: `Rezept erfolgreich gelöscht!`,
      life: 3000,
    })
  } catch (err) {
    error.value = 'Fehler beim Löschen des Rezepts.'
    toast.add({
      severity: 'error',
      summary: 'Fehler',
      detail: 'Konnte das Rezept nicht löschen.',
      life: 5000,
    })
    console.error('API Error:', err)
  } finally {
    isLoading.value = false
  }
}

const loadRecipes = async () => {
  error.value = null
  try {
    const response = await getRecipes()
    recipes.value = response.data
  } catch (err) {
    error.value = 'Fehler beim Laden der Rezepte. Stellen Sie sicher, dass das Backend läuft.'
    console.error('API Error:', err)
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
  <div class="p-container mt-5">
    <div class="flex justify-content-between align-items-center mb-5">
      <h1>Rezept-Feed 🍳</h1>
      <RouterLink to="/recipes/new">
        <Button label="Neues Rezept erstellen" icon="pi pi-plus" severity="success" />
      </RouterLink>
    </div>

    <div v-if="isLoading" class="flex justify-content-center p-5">
      <ProgressSpinner aria-label="Lade Rezepte" />
      <p class="ml-3 mt-2">Rezepte werden geladen...</p>
    </div>

    <div v-else-if="error">
      <Message severity="error" :closable="false">{{ error }}</Message>
    </div>

    <div v-else class="grid">
      <div v-for="recipe in recipes" :key="recipe.id" class="col-12 md:col-6 lg:col-4 xl:col-3">
        <RecipeCard :recipe="recipe" @show-detail="openRecipeDetail(recipe)" />
      </div>
      <Dialog
        v-model:visible="isDetailVisible"
        :header="selectedRecipeTitle"
        modal
        :style="{ width: '75vw' }"
        :draggable="false"
        :maximizable="true"
        @update:visible="closeRecipeDetail"
      >
        <template #header>
          <div class="flex justify-content-between align-items-center w-full mr-2">
            <h3>{{ selectedRecipeTitle }}</h3>
            <Button
              icon="pi pi-trash"
              severity="danger"
              text
              rounded
              @click.stop="confirmDeletion"
              aria-label="Rezept löschen"
            />
          </div>
        </template>
        <RecipeDetail :recipe-id="selectedRecipeId" />
      </Dialog>

      <div v-if="recipes.length === 0" class="col-12">
        <Message severity="info" :closable="false">
          Es wurden noch keine Rezepte gefunden. Erstellen Sie das erste!
        </Message>
      </div>
    </div>
  </div>
</template>

<style scoped>
.p-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 15px;
}
</style>
