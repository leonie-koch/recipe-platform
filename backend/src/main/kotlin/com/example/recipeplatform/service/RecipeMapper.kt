package com.example.recipeplatform.service

import com.example.recipeplatform.dto.IngredientDto
import com.example.recipeplatform.dto.RecipeRequestDto
import com.example.recipeplatform.dto.RecipeResponseDto
import com.example.recipeplatform.model.Recipe
import com.example.recipeplatform.model.RecipeIngredient
import org.springframework.stereotype.Service

@Service
class RecipeMapper(
    private val recipeService: RecipeService
) {

    fun toEntity(id: Long?, dto: RecipeRequestDto): Recipe? {

        val baseEntity: Recipe? = if (id != null) {
            recipeService.getRecipeById(id)
        } else {
            Recipe(
                title = "",
                description = "",
                instructions = emptyList(),
                recipeIngredients = emptyList()
            )
        }

        return baseEntity?.let { entity ->
            val newRecipeIngredients = dto.ingredients.map { ingredientDto ->
                val ingredient = recipeService.getOrCreateIngredient(ingredientDto.name)
                RecipeIngredient(
                    ingredient = ingredient,
                    quantity = ingredientDto.quantity,
                    unit = ingredientDto.unit
                )
            }

            return entity.copy(
                id = id ?: 0,
                title = dto.title,
                description = dto.description,
                instructions = dto.instructions,
                recipeIngredients = newRecipeIngredients // Ersetzt die alte Liste
            )
        }
    }

    /**
     * Konvertiert die komplexe Recipe Entity in ein flaches Response DTO.
     */
    fun fromEntity(entity: Recipe): RecipeResponseDto {
        val ingredientDtos = entity.recipeIngredients.map { recipeIngredient ->
            IngredientDto(
                name = recipeIngredient.ingredient.name,
                quantity = recipeIngredient.quantity,
                unit = recipeIngredient.unit
            )
        }

        return RecipeResponseDto(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            ingredients = ingredientDtos,
            instructions = entity.instructions
        )
    }
}