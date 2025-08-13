package com.example.nus.model

import java.time.LocalDateTime

class JournalEntry (
    val user: String?,
    val entryTitle: String,
    val entryText: String,
    val emotions: List<Emotion> = emptyList(),
    val mood: Int = 0,
    val lastSavedAt: LocalDateTime = LocalDateTime.now(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val date: LocalDateTime = LocalDateTime.now(),
    // 习惯数据
    val sleep: Double = 0.0,
    val water: Double = 0.0,
    val workHours: Double = 0.0,
    // 多时段心情数据
    val moodMorning: Int = 0,
    val moodAfternoon: Int = 0,
    val moodEvening: Int = 0,
    // 情绪反馈
    val emotionFeedback: Boolean = false
) {
    // Secondary dummy constructor
    constructor(user: String?, entryTitle: String) : this(
        user = user,
        entryTitle = entryTitle,
        entryText = "",
    )
}