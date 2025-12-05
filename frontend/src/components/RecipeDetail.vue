<script setup lang="ts">
import { ref, watch } from 'vue'
// Wichtig: Den korrekten Typ importieren
import type { RecipeResponseDto } from '@/types/RecipeDTOs'
import { getRecipeById } from '@/client/recipe-client'

import ProgressSpinner from 'primevue/progressspinner'
import Message from 'primevue/message'
import Divider from 'primevue/divider'

const props = defineProps<{
  recipeId: number | null
}>()

const recipe = ref<RecipeResponseDto | null>(null)
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

    <Message v-else-if="error" severity="error" :closable="false" class="p-4">{{ error }}</Message>

    <div v-else-if="recipe" class="p-4">
      <p class="mb-4 text-color-secondary">{{ recipe.description }}</p>

      <Divider>
        <div class="inline-flex align-items-center">
          <i class="pi pi-list mr-2"></i>
          <b>Zutaten</b>
        </div>
      </Divider>

      <div class="p-field mt-3">
        <ul class="list-disc ml-5 space-y-2">
          <li v-for="(item, index) in recipe.ingredients" :key="index">
            <span>{{ item.quantity }}</span>
            <span v-if="item.unit">{{ item.unit }}</span>
            {{ item.name }}
          </li>
        </ul>
      </div>

      <Divider class="mt-4">
        <div class="inline-flex align-items-center">
          <i class="pi pi-book mr-2"></i>
          <b>Zubereitung</b>
        </div>
      </Divider>

      <div class="p-field mt-3">
        <ol class="list-decimal ml-5 space-y-2 text-lg">
          <li v-for="(step, index) in recipe.instructions" :key="index">
            {{ step }}
          </li>
        </ol>
      </div>
    </div>

    <div v-else class="p-4">
      <Message severity="warn" :closable="false"
        >Keine Rezept-ID zum Laden ausgewählt oder das Laden ist fehlgeschlagen.</Message
      >
    </div>
  </div>
</template>
