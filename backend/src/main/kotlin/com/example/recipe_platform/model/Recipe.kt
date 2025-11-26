package com.example.recipe_platform.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Recipe(
    @Column(nullable = false)
    val title: String,

    @Column(nullable = false, length = 1000)
    val description: String,

    @Column(nullable = false, length = 5000)
    val ingredients: String,

    @Column(nullable = false, length = 5000)
    val instructions: String) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    // val createdByUserId: Long? = null
}