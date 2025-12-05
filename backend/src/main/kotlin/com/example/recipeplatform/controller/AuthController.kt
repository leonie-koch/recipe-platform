package com.example.recipeplatform.controller

import com.example.recipeplatform.dto.AuthResponseDto
import com.example.recipeplatform.dto.LoginRequestDto
import com.example.recipeplatform.dto.RegisterRequestDto
import com.example.recipeplatform.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody @Valid request: RegisterRequestDto): AuthResponseDto {
        return authService.register(request)
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequestDto): AuthResponseDto {
        return authService.login(request)
    }
}