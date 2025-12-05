
package com.example.recipeplatform.controller

import com.example.recipeplatform.dto.RecipeRequestDto
import com.example.recipeplatform.dto.RecipeResponseDto
import com.example.recipeplatform.model.Recipe
import com.example.recipeplatform.service.RecipeMapper
import com.example.recipeplatform.service.RecipeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/recipes")
class RecipeController(
    private val recipeService: RecipeService,
    private val recipeMapper: RecipeMapper
) {

    // GET /api/recipes
    @GetMapping
    fun listAll(): List<RecipeResponseDto> {
        val recipes: List<Recipe> = recipeService.getAllRecipes()

        return recipes.map { recipe ->
            recipeMapper.fromEntity(recipe)
        }
    }

    // GET /api/recipes/{id}
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<RecipeResponseDto> {
        val recipe = recipeService.getRecipeById(id)

        return if (recipe != null) {
            ResponseEntity.ok(recipeMapper.fromEntity(recipe))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    // POST /api/recipes
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody dto: RecipeRequestDto): RecipeResponseDto {
        val recipeEntity = recipeMapper.toEntity(null, dto)!!
        val savedEntity = recipeService.createRecipe(recipeEntity)
        return recipeMapper.fromEntity(savedEntity)
    }

    // PUT /api/recipes/{id}
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: RecipeRequestDto): ResponseEntity<RecipeResponseDto> {
        val updateCandidateEntity = recipeMapper.toEntity(id, dto)

        return updateCandidateEntity?.let { entity ->
            val resultEntity = recipeService.updateRecipe(id, entity)
            ResponseEntity.ok(recipeMapper.fromEntity(resultEntity!!))

        } ?: ResponseEntity.notFound().build()
    }

    // DELETE /api/recipes/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    fun delete(@PathVariable id: Long) {
        recipeService.deleteRecipe(id)
    }
}