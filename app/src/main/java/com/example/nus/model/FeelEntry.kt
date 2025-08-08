package com.example.nus.model

import java.time.LocalDate
import java.time.LocalDateTime

enum class FeelType {
    HAPPY,
    EXCITED,
    NEUTRAL,
    SAD,
    ANXIOUS
}

data class FeelEntry(
    val id: String = System.currentTimeMillis().toString(),
    val feelType: FeelType,
    val date: LocalDate = LocalDate.now(),
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val quote: String = ""
)
