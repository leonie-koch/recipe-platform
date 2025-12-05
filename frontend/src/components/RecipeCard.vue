<script setup lang="ts">
import Card from 'primevue/card'
import Button from 'primevue/button'
import type { RecipeResponseDto } from '@/types/Recipe'

defineProps<{
  recipe: RecipeResponseDto
}>()

const emit = defineEmits<(e: 'show-detail') => void>()

const openDetail = () => {
  emit('show-detail')
}
</script>

<template>
  <Card class="h-full">
    <template #header>
      <div style="height: 150px; background-color: #f0f0f0; border-radius: 8px 8px 0 0">
        <p class="text-center pt-5 text-gray-500">Bild-Placeholder</p>
      </div>
    </template>
    <template #title>
      {{ recipe.title }}
    </template>
    <template #subtitle> Kategorie </template>
    <template #content>
      <p>{{ recipe.description.substring(0, 80) }}...</p>
    </template>
    <template #footer>
      <Button
        label="Details ansehen"
        icon="pi pi-arrow-right"
        icon-pos="right"
        size="small"
        @click="openDetail()"
        class="w-full"
      />
    </template>
  </Card>
</template>
