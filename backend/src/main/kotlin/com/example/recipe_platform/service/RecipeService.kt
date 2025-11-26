package com.example.recipe_platform.service

import com.example.recipe_platform.model.Recipe
import com.example.recipe_platform.repository.RecipeRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RecipeService(private val repository: RecipeRepository) {

    fun getAllRecipes(): List<Recipe> = repository.findAll()

    fun getRecipeById(id: Long): Recipe? = repository.findByIdOrNull(id)

    fun createRecipe(recipe: Recipe): Recipe = repository.save(recipe)

    fun updateRecipe(id: Long, updatedRecipe: Recipe): Recipe? {
        val existingRecipe = repository.findByIdOrNull(id)

        return existingRecipe?.let {
            val savedRecipe = it.copy(
                title = updatedRecipe.title,
                description = updatedRecipe.description,
                ingredients = updatedRecipe.ingredients,
                instructions = updatedRecipe.instructions
                // Wichtig: Die ID des Originalrezepts wird beibehalten!
            )
            repository.save(savedRecipe)
        }
    }

    fun deleteRecipe(id: Long) {
        if (repository.existsById(id)) {
            repository.deleteById(id)
        }
    }
}