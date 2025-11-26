import axios from 'axios'
import type { Recipe } from '@/types/Recipe'
import type { RegisterRequest, LoginRequest, AuthResponse } from '@/types/Auth'

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api', // Basis-URL auf /api anpassen
  headers: {
    'Content-Type': 'application/json',
  },
})

apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jwt_token')
    if (token) {
      // Fügt den JWT-Header im Format Bearer <token> hinzu
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

// Auth
export const registerUser = (data: RegisterRequest) =>
  apiClient.post<AuthResponse>('/auth/register', data)
export const loginUser = (data: LoginRequest) => apiClient.post<AuthResponse>('/auth/login', data)

// GET /api/recipes
export const getRecipes = () => apiClient.get<Recipe[]>('/recipes')

// GET /api/recipes/{id}
export const getRecipeById = (id: number) => apiClient.get<Recipe>(`/recipes/${id}`)

// POST /api/recipes
export const createRecipe = (recipe: Omit<Recipe, 'id'>) =>
  apiClient.post<Recipe>('/recipes', recipe)

// PUT /api/recipes/{id}
export const updateRecipe = (id: number, updatedRecipe: Recipe) =>
  apiClient.put<Recipe>(`/recipes/${id}`, updatedRecipe)

// DELETE /api/recipes/{id}
export const deleteRecipe = (id: number) => apiClient.delete<void>(`/recipes/${id}`)
