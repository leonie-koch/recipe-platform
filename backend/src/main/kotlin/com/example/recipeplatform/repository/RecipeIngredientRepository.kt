package com.example.recipeplatform.repository

import com.example.recipeplatform.model.RecipeIngredient
import org.springframework.data.jpa.repository.JpaRepository

interface RecipeIngredientRepository : JpaRepository<RecipeIngredient, Long> {}