package com.example.nus.model

import java.time.LocalDateTime

data class HabitsEntry (
    val id: String,
    val user : User,
    val createdAt: LocalDateTime,
    val lastSavedAt: LocalDateTime,
    val sleep: Double,
    val water: Double,
    val workHours: Double
)