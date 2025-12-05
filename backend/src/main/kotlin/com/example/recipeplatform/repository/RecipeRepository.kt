package com.example.recipeplatform.repository

import com.example.recipeplatform.model.Recipe
import org.springframework.data.jpa.repository.JpaRepository

interface RecipeRepository : JpaRepository<Recipe, Long> {}