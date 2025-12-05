<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { createRecipe } from '@/client/recipe-client'
import type { RecipeRequestDto } from '@/types/Recipe'
import axios from 'axios'

import InputText from 'primevue/inputtext'
import Textarea from 'primevue/textarea'
import Button from 'primevue/button'
import Message from 'primevue/message'
import Card from 'primevue/card'

const router = useRouter()
const isSubmitting = ref(false)
const submitError = ref<string | null>(null)

const newRecipe = ref<RecipeRequestDto>({
  title: '',
  description: '',
  ingredients: [],
  instructions: [],
})

const instructionsText = computed({
  get() {
    return newRecipe.value.instructions.join('\n')
  },
  set(newValue: string) {
    newRecipe.value.instructions = newValue
      .split('\n')
      .map((s) => s.trim())
      .filter((s) => s.length > 0)
  },
})

const addManualIngredient = () => {
  newRecipe.value.ingredients.push({ name: '', quantity: '', unit: null })
}

const submitForm = async () => {
  submitError.value = null
  isSubmitting.value = true

  if (newRecipe.value.ingredients.length === 0) {
    submitError.value = 'Bitte fügen Sie mindestens eine Zutat hinzu.'
    isSubmitting.value = false
    return
  }

  try {
    await createRecipe(newRecipe.value)
    router.push('/')
  } catch (err) {
    if (axios.isAxiosError(err) && err.response?.status === 401) {
      submitError.value = 'Fehler: Nicht autorisiert. Bitte neu anmelden.'
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

          <div class="flex flex-col border-t pt-4">
            <label class="font-medium mb-1">Zutaten und Mengen</label>
            <small class="text-gray-500 mb-2"
              >Fügen Sie jede Zutat einzeln mit Menge und Einheit hinzu.</small
            >

            <div
              v-for="(item, index) in newRecipe.ingredients"
              :key="index"
              class="p-inputgroup mb-3"
            >
              <InputText
                v-model="item.name"
                placeholder="Name (z.B. Ei)"
                class="flex-grow-1"
                required
              />
              <InputText
                v-model="item.quantity"
                placeholder="Menge (z.B. 250)"
                class="w-20"
                required
              />
              <InputText v-model="item.unit" placeholder="Einheit (z.B. g)" class="w-20" />
              <Button
                icon="pi pi-times"
                severity="danger"
                type="button"
                @click="newRecipe.ingredients.splice(index, 1)"
              />
            </div>

            <Button
              label="Zutat hinzufügen"
              icon="pi pi-plus"
              severity="secondary"
              outlined
              @click="addManualIngredient"
              type="button"
              class="mt-2"
            />
          </div>

          <div class="flex flex-col border-t pt-4">
            <label for="instructions" class="font-medium mb-1">Zubereitung (Schritte)</label>
            <Textarea
              id="instructions"
              v-model="instructionsText"
              rows="8"
              required
              class="w-full"
            />
          </div>

          <Message v-if="submitError" severity="error" :closable="false">
            {{ submitError }}
          </Message>

          <div class="flex justify-end pt-4">
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
