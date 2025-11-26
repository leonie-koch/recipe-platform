package com.example.recipe_platform.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfiguration {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }

            .authorizeHttpRequests { authorize ->
                authorize
                    .anyRequest().authenticated()
            }
            .httpBasic { }

        return http.build()
    }
}