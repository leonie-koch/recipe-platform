package com.example.recipe_platform.controller


import com.example.recipe_platform.model.Recipe
import com.example.recipe_platform.service.RecipeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/recipes")
class RecipeController(private val service: RecipeService) {

    // GET /api/recipes
    @GetMapping
    fun listAll(): List<Recipe> = service.getAllRecipes()

    // GET /api/recipes/{id}
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Recipe> {
        val recipe = service.getRecipeById(id)
        return if (recipe != null) {
            ResponseEntity.ok(recipe)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    // POST /api/recipes
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody recipe: Recipe): Recipe = service.createRecipe(recipe)

    // PUT /api/recipes/{id}
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody recipe: Recipe): ResponseEntity<Recipe> {
        val updatedRecipe = service.updateRecipe(id, recipe)
        return if (updatedRecipe != null) {
            ResponseEntity.ok(updatedRecipe)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    // DELETE /api/recipes/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        service.deleteRecipe(id)
    }
}