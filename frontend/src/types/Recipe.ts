/**
 * Definition der Struktur für eine einzelne Zutat.
 */
export interface IngredientDto {
  name: string
  quantity: string
  unit: string | null
}

/**
 * DTO für die API-Antwort (GET / PUT / POST Response)
 */
export interface RecipeResponseDto {
  id: number
  title: string
  description: string
  ingredients: IngredientDto[]
  instructions: string[]
}

/**
 * DTO für die API-Anfrage (POST / PUT Request)
 * Entspricht der ResponseDto, aber ohne ID.
 */
export type RecipeRequestDto = Omit<RecipeResponseDto, 'id'>

/**
 * DTO für die KI-Parsing-Anfrage (Text-Eingabe)
 */
export interface ParseRequest {
  text: string
}

/**
 * DTO für die KI-Antwort (POST /api/recipes/parse)
 * Dieses Format kommt direkt vom Kotlin Backend (AIParsingService).
 */
export interface ParsedRecipeDTO {
  title: string
  description: string
  ingredients: IngredientDto[]
  steps: string[]
}
