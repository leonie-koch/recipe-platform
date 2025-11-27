<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createRecipe } from '@/client/recipe-client'
import type { Recipe } from '@/types/Recipe'

import InputText from 'primevue/inputtext'
import Textarea from 'primevue/textarea'
import Button from 'primevue/button'
import Message from 'primevue/message'
import Card from 'primevue/card'

import axios from 'axios'

const router = useRouter()

const newRecipe = ref<Omit<Recipe, 'id'>>({
  title: '',
  description: '',
  ingredients: '',
  instructions: '',
})

const isSubmitting = ref(false)
const submitError = ref<string | null>(null)

const submitForm = async () => {
  submitError.value = null
  isSubmitting.value = true

  try {
    await createRecipe(newRecipe.value)
    router.push('/')
  } catch (err) {
    if (axios.isAxiosError(err) && err.response?.status === 401) {
      submitError.value = 'Fehler: Nicht autorisiert (401). Bitte Basic Auth im API-Client prüfen.'
    } else {
      submitError.value = 'Fehler beim Erstellen des Rezepts.'
    }
    console.error('Submission Error:', err)
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <div class="w-full flex justify-center px-4 py-10">
    <Card class="w-full max-w-3xl shadow-md">
      <template #title>
        <h1 class="text-2xl font-semibold">Neues Rezept erstellen</h1>
      </template>

      <template #content>
        <form @submit.prevent="submitForm" class="space-y-6">
          <div class="flex flex-col">
            <label for="title" class="font-medium mb-1">Titel</label>
            <InputText id="title" v-model="newRecipe.title" required class="w-full" />
          </div>

          <div class="flex flex-col">
            <label for="description" class="font-medium mb-1">Kurzbeschreibung</label>
            <Textarea
              id="description"
              v-model="newRecipe.description"
              rows="3"
              required
              class="w-full"
            />
            <small class="text-gray-500">Wird im Feed angezeigt.</small>
          </div>

          <div class="flex flex-col">
            <label for="ingredients" class="font-medium mb-1">Zutaten</label>
            <Textarea
              id="ingredients"
              v-model="newRecipe.ingredients"
              rows="6"
              required
              class="w-full"
            />
            <small class="text-gray-500">Tipp: Zutaten durch Zeilenumbrüche trennen.</small>
          </div>

          <div class="flex flex-col">
            <label for="instructions" class="font-medium mb-1">Zubereitung</label>
            <Textarea
              id="instructions"
              v-model="newRecipe.instructions"
              rows="8"
              required
              class="w-full"
            />
          </div>

          <Message v-if="submitError" severity="error" :closable="false">
            {{ submitError }}
          </Message>

          <div class="flex justify-end">
            <Button
              type="submit"
              label="Rezept speichern"
              icon="pi pi-check"
              :loading="isSubmitting"
              :disabled="isSubmitting"
            />
          </div>
        </form>
      </template>
    </Card>
  </div>
</template>
