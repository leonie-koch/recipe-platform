<script setup lang="ts">
import { ref, watch } from 'vue'
import { getRecipeById } from '@/client/recipe-client'
import type { Recipe } from '@/types/Recipe'

import ProgressSpinner from 'primevue/progressspinner'
import Message from 'primevue/message'
import Divider from 'primevue/divider'

const props = defineProps<{
  recipeId: number | null
}>()

const recipe = ref<Recipe | null>(null)
const isLoading = ref(false)
const error = ref<string | null>(null)

const fetchRecipeDetail = async (id: number) => {
  if (isLoading.value) return

  isLoading.value = true
  error.value = null

  try {
    const response = await getRecipeById(id)
    recipe.value = response.data
  } catch (err) {
    error.value = 'Fehler beim Laden der Rezeptdetails. Prüfen Sie die Verbindung und die ID.'
    console.error('Detail API Error:', err)
  } finally {
    isLoading.value = false
  }
}

watch(
  () => props.recipeId,
  (newId) => {
    if (newId) {
      fetchRecipeDetail(newId)
    } else {
      recipe.value = null
    }
  },
  { immediate: true },
)
</script>

<template>
  <div>
    <div v-if="isLoading" class="flex justify-content-center p-5">
      <ProgressSpinner />
      <p class="ml-3 mt-2">Rezeptdetails werden geladen...</p>
    </div>

    <Message v-else-if="error" severity="error" :closable="false">{{ error }}</Message>

    <div v-else-if="recipe" class="p-4">
      <p class="mb-4 text-color-secondary">{{ recipe.description }}</p>

      <Divider>
        <div class="inline-flex align-items-center">
          <i class="pi pi-list mr-2"></i>
          <b>Zutaten</b>
        </div>
      </Divider>

      <div class="white-space-pre-line text-lg">
        {{ recipe.ingredients }}
      </div>

      <Divider class="mt-4">
        <div class="inline-flex align-items-center">
          <i class="pi pi-book mr-2"></i>
          <b>Zubereitung</b>
        </div>
      </Divider>

      <div class="white-space-pre-line text-lg">
        {{ recipe.instructions }}
      </div>
    </div>

    <div v-else class="p-4">
      <Message severity="warn" :closable="false">Keine Rezept-ID zum Laden ausgewählt.</Message>
    </div>
  </div>
</template>

<style scoped>
/* Stellt sicher, dass Zeilenumbrüche im Textarea (Zubereitung) als Zeilenumbrüche gerendert werden */
.white-space-pre-line {
  white-space: pre-line;
}
</style>
