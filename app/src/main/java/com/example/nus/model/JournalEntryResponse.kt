package com.example.nus.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 后端Emotion对象的响应模型
 */
data class EmotionResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("emotionLabel")
    val emotionLabel: String,
    @SerializedName("iconAddress")
    val iconAddress: String? = null
)

data class JournalEntryResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("userId")
    val userId: String? = null, // 允许userId为null
    @SerializedName("mood")
    val mood: Int,
    @SerializedName("entryTitle")
    val entryTitle: String,
    @SerializedName("entryText")
    val entryText: String,
    @SerializedName("emotions")
    val emotions: List<EmotionResponse>,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("lastSavedAt")
    val lastSavedAt: String? = null,
    @SerializedName("archived")
    val archived: Boolean = false,
    // 习惯数据
    @SerializedName("sleep")
    val sleep: Double = 0.0,
    @SerializedName("water")
    val water: Double = 0.0,
    @SerializedName("workHours")
    val workHours: Double = 0.0,
    // 多时段心情数据
    @SerializedName("moodMorning")
    val moodMorning: Int = 0,
    @SerializedName("moodAfternoon")
    val moodAfternoon: Int = 0,
    @SerializedName("moodEvening")
    val moodEvening: Int = 0,
    // 情绪反馈
    @SerializedName("emotionFeedback")
    val emotionFeedback: Boolean = false
) {
    /**
     * 转换为JournalEntry对象
     */
    fun toJournalEntry(): JournalEntry {
        val parsedCreatedAt = try {
            createdAt?.let {
                LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            } ?: LocalDateTime.now()
        } catch (e: Exception) {
            LocalDateTime.now()
        }

        val parsedLastSavedAt = try {
            lastSavedAt?.let {
                LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            } ?: parsedCreatedAt
        } catch (e: Exception) {
            parsedCreatedAt
        }

        // 将EmotionResponse对象转换为Emotion对象列表
        val emotionObjects = emotions.mapNotNull { emotionResponse ->
            try {
                // 根据Emotion类的构造函数创建对象
                Emotion(
                    id = emotionResponse.id,
                    emotionLabel = emotionResponse.emotionLabel,
                    iconAddress = emotionResponse.iconAddress ?: "" // 如果为null则使用空字符串
                )
            } catch (e: Exception) {
                println("Error converting emotion: ${e.message}")
                null
            }
        }

        return JournalEntry(
            user = userId ?: "unknown", // 如果userId为null，使用默认值
            entryTitle = entryTitle,
            entryText = entryText,
            emotions = emotionObjects,
            mood = mood,
            lastSavedAt = parsedLastSavedAt,
            createdAt = parsedCreatedAt,
            date = parsedCreatedAt,
            // 习惯数据
            sleep = sleep,
            water = water,
            workHours = workHours,
            // 多时段心情数据
            moodMorning = moodMorning,
            moodAfternoon = moodAfternoon,
            moodEvening = moodEvening,
            // 情绪反馈
            emotionFeedback = emotionFeedback
        )
    }
}
