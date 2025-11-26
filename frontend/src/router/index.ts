import { createRouter, createWebHistory } from 'vue-router'
import RecipeFeed from '@/views/RecipeFeed.vue'
import RecipeForm from '@/views/RecipeForm.vue'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'

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
    {
      path: '/login',
      name: 'login',
      component: Login,
    },
    {
      path: '/register',
      name: 'register',
      component: Register,
    },
  ],
})

export default router
