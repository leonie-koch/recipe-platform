package com.example.recipe_platform.controller

import com.example.recipe_platform.dto.AuthResponse
import com.example.recipe_platform.dto.LoginRequest
import com.example.recipe_platform.dto.RegisterRequest
import com.example.recipe_platform.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody @Valid request: RegisterRequest): AuthResponse {
        return authService.register(request)
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequest): AuthResponse {
        return authService.login(request)
    }
}