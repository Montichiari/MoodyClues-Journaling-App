package com.example.nus.model

import java.time.LocalDate

data class LifestyleEntry(
    val id: String = System.currentTimeMillis().toString(),
    val date: LocalDate = LocalDate.now(),
    val sleepHours: Float = 0f,
    val waterGlasses: Int = 0,
    val didOvertime: Boolean = false,
    val exerciseMinutes: Int = 0,
    val stressLevel: Int = 0, // 0-10 scale
    val socialInteractionHours: Float = 0f,
    val notes: String = ""
) 