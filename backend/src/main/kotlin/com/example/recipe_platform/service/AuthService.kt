package com.example.recipe_platform.service

import com.example.recipe_platform.dto.AuthResponse
import com.example.recipe_platform.dto.LoginRequest
import com.example.recipe_platform.dto.RegisterRequest
import com.example.recipe_platform.model.User
import com.example.recipe_platform.repository.UserRepository
import com.example.recipe_platform.security.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {
    fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.findByUsername(request.username) != null) {
            throw IllegalArgumentException("Username already taken")
        }

        val user = User(
            usernameField = request.username,
            passwordHash = passwordEncoder.encode(request.password)!!,
            roles = setOf("USER")
        )
        val savedUser = userRepository.save(user)

        val jwtToken = jwtService.generateToken(savedUser)
        return AuthResponse(token = jwtToken)
    }

    fun login(request: LoginRequest): AuthResponse {
        // 1. Authentifizierung mit Username/Passwort
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )
        // 2. Benutzer abrufen und JWT generieren
        val user = userRepository.findByUsername(request.username)
            ?: throw RuntimeException("User not found after successful auth")

        val jwtToken = jwtService.generateToken(user)
        return AuthResponse(token = jwtToken)
    }
}