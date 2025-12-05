package com.example.recipeplatform.dto

data class RecipeRequestDto(
    val title: String,
    val description: String,
    val ingredients: List<IngredientDto>,
    val instructions: List<String>
)