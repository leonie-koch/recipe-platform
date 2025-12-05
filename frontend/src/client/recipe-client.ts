import axios from 'axios'
import type { ParsedRecipeDTO, ParseRequest, RecipeResponseDto } from '@/types/Recipe'
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

apiClient.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('jwt_token')
      globalThis.location.href = '/login'
    }
    return Promise.reject(error)
  },
)

// Auth
export const registerUser = (data: RegisterRequest) =>
  apiClient.post<AuthResponse>('/auth/register', data)
export const loginUser = (data: LoginRequest) => apiClient.post<AuthResponse>('/auth/login', data)

// GET /api/recipes
export const getRecipes = () => apiClient.get<RecipeResponseDto[]>('/recipes')

// GET /api/recipes/{id}
export const getRecipeById = (id: number) => apiClient.get<RecipeResponseDto>(`/recipes/${id}`)

// POST /api/recipes
export const createRecipe = (recipe: Omit<RecipeResponseDto, 'id'>) =>
  apiClient.post<RecipeResponseDto>('/recipes', recipe)

// PUT /api/recipes/{id}
export const updateRecipe = (id: number, updatedRecipe: RecipeResponseDto) =>
  apiClient.put<RecipeResponseDto>(`/recipes/${id}`, updatedRecipe)

// DELETE /api/recipes/{id}
export const deleteRecipe = (id: number) => apiClient.delete<void>(`/recipes/${id}`)

// AI Parsing
/**
 * Sendet Freitext an das Backend zur KI-Analyse.
 * @param data Enthält den Freitext im Feld 'text'.
 * @returns ParsedRecipeDTO, die die strukturierte Version des Rezepts enthält.
 */
export const parseRecipe = (data: ParseRequest) =>
  apiClient.post<ParsedRecipeDTO>('/recipes/parse', data)
