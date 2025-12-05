package com.example.recipeplatform.repository

import com.example.recipeplatform.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUserName(userName: String): User?
}