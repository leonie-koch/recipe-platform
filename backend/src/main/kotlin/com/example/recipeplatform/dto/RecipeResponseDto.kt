package com.example.recipeplatform.dto

data class RecipeResponseDto(
    val id: Long,
    val title: String,
    val description: String,
    val ingredients: List<IngredientDto>,
    val instructions: List<String>
)