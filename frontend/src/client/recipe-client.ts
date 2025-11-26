import axios from 'axios'
import type { Recipe } from '@/types/Recipe'

const basicAuth = 'Basic ' + btoa('user:977b3bb1-cc26-46f3-89ee-fd2c285f67da')

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api/recipes',
  headers: {
    'Content-Type': 'application/json',
    Authorization: basicAuth,
  },
})

// GET /api/recipes
export const getRecipes = () => apiClient.get<Recipe[]>('')

// GET /api/recipes/{id}
export const getRecipeById = (id: number) => apiClient.get<Recipe>(`/${id}`)

// POST /api/recipes
export const createRecipe = (recipe: Omit<Recipe, 'id'>) => apiClient.post<Recipe>('', recipe)

// PUT /api/recipes/{id}
export const updateRecipe = (id: number, updatedRecipe: Recipe) =>
  apiClient.put<Recipe>(`/${id}`, updatedRecipe)

// DELETE /api/recipes/{id}
export const deleteRecipe = (id: number) => apiClient.delete<void>(`/${id}`)
