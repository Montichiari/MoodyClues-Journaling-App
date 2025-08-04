package com.example.nus.model

import java.time.LocalDate
import java.time.LocalDateTime

enum class MoodType {
    VERY_GOOD,
    GOOD,
    NEUTRAL,
    BAD,
    VERY_BAD
}

enum class TimeOfDay {
    MORNING,
    AFTERNOON,
    EVENING
}

data class MoodEntry(
    val id: String = System.currentTimeMillis().toString(),
    val mood: MoodType,
    val timeOfDay: TimeOfDay,
    val date: LocalDate = LocalDate.now(),
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val notes: String = ""
) 