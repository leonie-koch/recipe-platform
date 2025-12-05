package com.example.recipeplatform.service

import com.example.recipeplatform.dto.AuthResponseDto
import com.example.recipeplatform.dto.LoginRequestDto
import com.example.recipeplatform.dto.RegisterRequestDto
import com.example.recipeplatform.model.User
import com.example.recipeplatform.repository.UserRepository
import com.example.recipeplatform.security.JwtService
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
    fun register(request: RegisterRequestDto): AuthResponseDto {
        if (userRepository.findByUserName(request.username) != null) {
            throw IllegalArgumentException("Username already taken")
        }

        val user = User(
            userName = request.username,
            passwordHash = passwordEncoder.encode(request.password)!!,
            roles = setOf("USER")
        )
        val savedUser = userRepository.save(user)

        val jwtToken = jwtService.generateToken(savedUser)
        return AuthResponseDto(token = jwtToken)
    }

    fun login(request: LoginRequestDto): AuthResponseDto {
        // 1. Authentifizierung mit Username/Passwort
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )
        // 2. Benutzer abrufen und JWT generieren
        val user = userRepository.findByUserName(request.username)
            ?: throw RuntimeException("User not found after successful auth")

        val jwtToken = jwtService.generateToken(user)
        return AuthResponseDto(token = jwtToken)
    }
}