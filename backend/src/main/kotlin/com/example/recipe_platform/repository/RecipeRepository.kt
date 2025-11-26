package com.example.recipe_platform.repository

import com.example.recipe_platform.model.Recipe
import org.springframework.data.jpa.repository.JpaRepository

interface RecipeRepository : JpaRepository<Recipe, Long> {}