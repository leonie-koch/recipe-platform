package com.example.recipeplatform.service

import com.example.recipeplatform.model.Ingredient
import com.example.recipeplatform.model.Recipe
import com.example.recipeplatform.repository.IngredientRepository
import com.example.recipeplatform.repository.RecipeIngredientRepository
import com.example.recipeplatform.repository.RecipeRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecipeService(private val repository: RecipeRepository, private val ingredientRepository: IngredientRepository) {

    fun getAllRecipes(): List<Recipe> = repository.findAll()

    fun getRecipeById(id: Long): Recipe? = repository.findByIdOrNull(id)

    fun createRecipe(recipe: Recipe): Recipe = repository.save(recipe)

    @Transactional
    fun updateRecipe(id: Long, updatedRecipe: Recipe): Recipe? {
        val existingRecipe = repository.findByIdOrNull(id)

        return existingRecipe?.let {
            val savedRecipe = it.copy(
                title = updatedRecipe.title,
                description = updatedRecipe.description,
                recipeIngredients = updatedRecipe.recipeIngredients,
                instructions = updatedRecipe.instructions
            )
            repository.save(savedRecipe)
        }
    }

    fun deleteRecipe(id: Long) {
        if (repository.existsById(id)) {
            repository.deleteById(id)
        }
    }

    @Transactional
    fun getOrCreateIngredient(name: String): Ingredient {
        // 1. Versuche, die Zutat zu finden (case-insensitive search)
        var ingredient = ingredientRepository.findByNameIgnoreCase(name)

        // 2. Wenn nicht gefunden, erstelle eine neue
        if (ingredient == null) {
            ingredient = ingredientRepository.save(Ingredient(name = name))
        }
        return ingredient
    }
}