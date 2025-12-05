package com.example.recipeplatform.dto
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class RegisterRequestDto(
    @field:NotNull(message = "Username ist erforderlich")
    @field:NotBlank(message = "Username darf nicht leer sein")
    val username: String,

    @field:NotNull(message = "Passwort ist erforderlich")
    @field:NotBlank(message = "Passwort ist erforderlich")
    val password: String)
