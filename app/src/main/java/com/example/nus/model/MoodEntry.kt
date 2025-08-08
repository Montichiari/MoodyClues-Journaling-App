package com.example.nus.model

import java.time.LocalDate
import java.time.LocalDateTime

enum class MoodType(val value: Int) {
    VERY_BAD(1),
    BAD(2),
    NEUTRAL(3),
    GOOD(4),
    VERY_GOOD(5);

    companion object {
        fun fromInt(value: Int): MoodType {
            return values().find { it.value == value } ?: NEUTRAL
        }
    }
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
    val notes: String = "",
    // 新增字段以匹配后端
    val entryTitle: String = "",
    val entryText: String = "",
    val emotions: List<String> = emptyList(),
    val userId: String = ""
) {
    // 转换为后端请求格式
    fun toJournalEntryRequest(): JournalEntryRequest {
        return JournalEntryRequest(
            userId = userId,
            mood = mood.value,
            entryTitle = entryTitle,
            entryText = entryText,
            emotions = emotions
        )
    }

    companion object {
        // 从后端响应创建MoodEntry
        fun fromJournalEntryResponse(response: JournalEntryResponse): MoodEntry {
            return MoodEntry(
                id = response.id,
                mood = MoodType.fromInt(response.mood),
                timeOfDay = TimeOfDay.MORNING, // 默认值，后端没有这个字段
                entryTitle = response.entryTitle,
                entryText = response.entryText,
                emotions = response.emotions,
                userId = response.userId
            )
        }
    }
}