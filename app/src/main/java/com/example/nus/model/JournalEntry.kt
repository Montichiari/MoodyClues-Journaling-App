package com.example.nus.model

import java.time.LocalDateTime

class JournalEntry (
    val user: String,
    val entryTitle: String,
    val entryText: String,
    val emotions: List<Emotion> = emptyList(),
    val mood: Int = 0,
    val lastSavedAt: LocalDateTime = LocalDateTime.now(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val date: LocalDateTime = LocalDateTime.now()
) {
    // Secondary dummy constructor
    constructor(user: String, entryTitle: String) : this(
        user = user,
        entryTitle = entryTitle,
        entryText = "",
    )
}