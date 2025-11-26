package com.example.recipe_platform.config

import com.example.recipe_platform.repository.UserRepository
import com.example.recipe_platform.security.JwtAuthenticationFilter
import com.example.recipe_platform.security.JwtService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userRepository: UserRepository,
) {
    @Bean
    fun jwtAuthFilter(
        // Wir injizieren die benötigten Beans direkt in die Methode
        userDetailsService: UserDetailsService,
        jwtService: JwtService
    ): JwtAuthenticationFilter {
        // Erstellen Sie die Instanz und übergeben Sie die notwendigen Abhängigkeiten
        return JwtAuthenticationFilter(jwtService, userDetailsService)
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity, jwtAuthFilter: JwtAuthenticationFilter,
                            authenticationProvider: AuthenticationProvider): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/recipes/**").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService = UserDetailsService { username ->
        userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found: $username")
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider(userDetailsService())
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }
}