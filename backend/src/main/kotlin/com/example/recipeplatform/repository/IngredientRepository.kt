package com.example.recipeplatform.repository

import com.example.recipeplatform.model.Ingredient
import com.example.recipeplatform.model.RecipeIngredient
import org.springframework.data.jpa.repository.JpaRepository

interface IngredientRepository : JpaRepository<Ingredient, Long> {
    fun findByNameIgnoreCase(name: String): Ingredient?}