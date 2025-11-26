import { createRouter, createWebHistory } from 'vue-router'
import RecipeFeed from '@/views/RecipeFeed.vue'
import RecipeForm from '@/views/RecipeForm.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'feed',
      component: RecipeFeed,
    },
    {
      path: '/recipes/new',
      name: 'new-recipe',
      component: RecipeForm,
    },
  ],
})

export default router
