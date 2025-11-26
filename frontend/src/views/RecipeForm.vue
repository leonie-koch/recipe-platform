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

    alert('Rezept erfolgreich gespeichert!')
    router.push('/')
  } catch (err) {
    if (axios.isAxiosError(err) && err.response?.status === 401) {
      submitError.value = 'Fehler: Nicht autorisiert (401). Bitte Basic Auth im API-Client prüfen.'
    } else {
      submitError.value = 'Fehler beim Erstellen des Rezepts. Prüfen Sie die Konsolenausgabe.'
    }
    console.error('Submission Error:', err)
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <div class="flex justify-content-center mt-5">
    <Card class="w-full md:w-8 lg:w-6">
      <template #title> Neues Rezept erstellen </template>
      <template #content>
        <form @submit.prevent="submitForm" class="p-fluid">
          <div class="field mb-4">
            <label for="title">Titel</label>
            <InputText
              id="title"
              v-model="newRecipe.title"
              type="text"
              required
              aria-describedby="title-help"
            />
          </div>

          <div class="field mb-4">
            <label for="description">Kurzbeschreibung</label>
            <Textarea id="description" v-model="newRecipe.description" rows="3" required />
            <small id="description-help">Wird im Feed angezeigt.</small>
          </div>

          <div class="field mb-4">
            <label for="ingredients">Zutaten</label>
            <Textarea id="ingredients" v-model="newRecipe.ingredients" rows="6" required />
            <small>Tipp: Trennen Sie Zutaten durch Zeilenumbrüche.</small>
          </div>

          <div class="field mb-5">
            <label for="instructions">Zubereitungsanweisungen</label>
            <Textarea id="instructions" v-model="newRecipe.instructions" rows="8" required />
          </div>

          <Message v-if="submitError" severity="error" :closable="false">{{ submitError }}</Message>

          <Button
            type="submit"
            label="Rezept speichern"
            icon="pi pi-check"
            :loading="isSubmitting"
            :disabled="isSubmitting"
            class="mt-3"
          />
        </form>
      </template>
    </Card>
  </div>
</template>

<style scoped>
.form-container {
  max-width: 800px;
}
</style>
