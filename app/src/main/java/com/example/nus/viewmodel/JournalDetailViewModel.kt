package com.example.nus.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nus.api.ApiClient
import com.example.nus.model.JournalEntry
import com.example.nus.model.Emotion
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class JournalDetailViewModel : ViewModel() {

    // 当前日志条目
    val journalEntry = mutableStateOf<JournalEntry?>(null)
    
    // 加载状态
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    /**
     * 从后端加载特定的日志条目详情
     */
    fun loadJournalEntry(
        journalUserId: String,
        entryId: String,
        counsellorId: String
    ) {
        viewModelScope.launch {
            isLoading.value = true
            error.value = null
            
            try {
                println("JournalDetailViewModel: Loading journal entry: $entryId for user: $journalUserId, counsellor: $counsellorId")
                
                val response = ApiClient.counsellorClientApiService
                    .getJournalEntryAndroid(journalUserId, entryId, counsellorId)
                
                if (response.isSuccessful) {
                    val journalEntryResponse = response.body()
                    if (journalEntryResponse != null) {
                        journalEntry.value = journalEntryResponse.toJournalEntry()
                        println("JournalDetailViewModel: Successfully loaded journal entry: ${journalEntryResponse.entryTitle}")
                    } else {
                        error.value = "Journal entry not found"
                    }
                } else {
                    val errorMessage = "Failed to load journal entry: ${response.code()} - ${response.message()}"
                    println("JournalDetailViewModel: API error: $errorMessage")
                    error.value = errorMessage
                }
            } catch (e: Exception) {
                val errorMessage = "Network error: ${e.message}"
                println("JournalDetailViewModel: Exception: $errorMessage")
                error.value = errorMessage
            } finally {
                isLoading.value = false
            }
        }
    }

    /**
     * 设置日志条目（用于从列表传递的情况）
     */
    fun setJournalEntry(entry: JournalEntry) {
        journalEntry.value = entry
        error.value = null
    }

    /**
     * 清除错误状态
     */
    fun clearError() {
        error.value = null
    }

    /**
     * 重试加载
     */
    fun retry(
        journalUserId: String,
        entryId: String,
        counsellorId: String
    ) {
        loadJournalEntry(journalUserId, entryId, counsellorId)
    }

    /**
     * 创建测试数据以展示完整功能
     */
    fun loadTestDataWithFullFeatures() {
        val testEmotions = listOf(
            Emotion(id = "1", emotionLabel = "Happy", iconAddress = ""),
            Emotion(id = "2", emotionLabel = "Excited", iconAddress = ""),
            Emotion(id = "3", emotionLabel = "Grateful", iconAddress = "")
        )

        val testEntry = JournalEntry(
            user = "ALICE",
            entryTitle = "ALICE D00 T1",
            entryText = "Morning reflection (ALICE), D-0\n\nToday was a wonderful day filled with positive energy. I woke up feeling refreshed after a good night's sleep. Had a productive work session and made sure to stay hydrated throughout the day. The weather was perfect for a morning walk, which really helped set a positive tone for the rest of the day.\n\nI'm grateful for the small moments of joy and the progress I'm making on my personal goals.",
            emotions = testEmotions,
            mood = 8,
            date = LocalDateTime.now(),
            createdAt = LocalDateTime.now(),
            lastSavedAt = LocalDateTime.now(),
            // 习惯数据
            sleep = 8.0,
            water = 2.3,
            workHours = 7.0,
            // 多时段心情数据
            moodMorning = 0,
            moodAfternoon = 0,
            moodEvening = 0,
            // 情绪反馈
            emotionFeedback = true
        )

        journalEntry.value = testEntry
        error.value = null
        isLoading.value = false
    }
}
