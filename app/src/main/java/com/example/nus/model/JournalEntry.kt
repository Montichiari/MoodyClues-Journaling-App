package com.example.nus.model

import java.time.LocalDateTime

data class JournalEntry(
    val user : User,
    val entryTitle: String,
    val entryText: String,
    val emotions:List<Emotion>,
    val mood: Int,
    val lastSavedAt: LocalDateTime,
    val createdAt: LocalDateTime
)
