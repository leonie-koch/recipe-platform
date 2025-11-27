package com.example.recipe_platform.repository

import com.example.recipe_platform.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUserName(userName: String): User?
}