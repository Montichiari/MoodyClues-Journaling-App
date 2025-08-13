package com.example.nus.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nus.api.ApiClient
import com.example.nus.model.JournalEntry
import com.example.nus.model.Emotion
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class JournalViewModel : ViewModel() {

    // Backing list of model.JournalEntry
    private val _journalList = mutableStateListOf<JournalEntry>()
    val journalList: List<JournalEntry> get() = _journalList

    // 加载状态
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    // 当前客户端和咨询师ID
    private var currentClientId: String = ""
    private var currentCounsellorId: String = ""

    /**
     * 为特定客户端加载日志条目
     */
    fun loadForClient(clientId: String, counsellorId: String = "") {
        currentClientId = clientId
        currentCounsellorId = counsellorId
        
        if (counsellorId.isEmpty()) {
            // 如果没有提供counsellorId，加载测试数据
            loadTestData(clientId)
            return
        }
        
        viewModelScope.launch {
            isLoading.value = true
            error.value = null
            
            try {
                println("JournalViewModel: Loading journal entries for client: $clientId, counsellor: $counsellorId")
                
                val response = ApiClient.counsellorClientApiService
                    .listClientJournalEntriesAndroid(clientId, counsellorId)
                
                if (response.isSuccessful) {
                    val journalEntries = response.body() ?: emptyList()
                    _journalList.clear()
                    _journalList.addAll(journalEntries.map { it.toJournalEntry() })
                    
                    println("JournalViewModel: Successfully loaded ${journalEntries.size} journal entries")
                } else {
                    val errorMessage = "Failed to load journal entries: ${response.code()} - ${response.message()}"
                    println("JournalViewModel: API error: $errorMessage")
                    error.value = errorMessage
                    
                    // 如果API失败，加载测试数据作为后备
                    loadTestData(clientId)
                }
            } catch (e: Exception) {
                val errorMessage = "Network error: ${e.message}"
                println("JournalViewModel: Exception: $errorMessage")
                error.value = errorMessage
                
                // 如果网络异常，加载测试数据作为后备
                loadTestData(clientId)
            } finally {
                isLoading.value = false
            }
        }
    }

    /**
     * 加载测试数据作为后备
     */
    private fun loadTestData(clientId: String) {
        _journalList.clear()
        val now = LocalDateTime.now()

        val testEmotions = listOf(
            Emotion(id = "1", emotionLabel = "Happy", iconAddress = ""),
            Emotion(id = "2", emotionLabel = "Excited", iconAddress = ""),
            Emotion(id = "3", emotionLabel = "Grateful", iconAddress = "")
        )

        _journalList.addAll(
            listOf(
                JournalEntry(
                    user = clientId,
                    entryTitle = "ALICE D00 T3",
                    entryText = "Evening recap (ALICE), D-0\n\nToday was a wonderful day filled with positive energy. I woke up feeling refreshed after a good night's sleep. Had a productive work session and made sure to stay hydrated throughout the day. The weather was perfect for a morning walk, which really helped set a positive tone for the rest of the day.\n\nI'm grateful for the small moments of joy and the progress I'm making on my personal goals.",
                    emotions = testEmotions,
                    mood = 5, // Neutral mood
                    date = now,
                    createdAt = now,
                    lastSavedAt = now,
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
                ),
                JournalEntry(
                    user = clientId,
                    entryTitle = "Morning Reflection",
                    entryText = "Had some amazing gabagool today. Feeling grateful for the simple pleasures in life.",
                    emotions = listOf(
                        Emotion(id = "4", emotionLabel = "Content", iconAddress = ""),
                        Emotion(id = "5", emotionLabel = "Peaceful", iconAddress = "")
                    ),
                    mood = 7, // Good mood
                    date = now.minusDays(1),
                    createdAt = now.minusDays(1),
                    lastSavedAt = now.minusDays(1),
                    // 习惯数据
                    sleep = 7.5,
                    water = 1.8,
                    workHours = 6.5,
                    // 多时段心情数据
                    moodMorning = 6,
                    moodAfternoon = 7,
                    moodEvening = 8,
                    // 情绪反馈
                    emotionFeedback = true
                )
            )
        )
        println("JournalViewModel: Loaded test data with complete features for client: $clientId")
    }

    /**
     * Add a new entry for the given user.
     */
    fun addEntry(
        user: String,
        title: String,
        text: String,
        date: LocalDateTime = LocalDateTime.now()
    ) {
        _journalList.add(
            JournalEntry(
                user = user,
                entryTitle = title,
                entryText = text,
                date = date
            )
        )
    }

    /**
     * Look up an entry by title.
     */
    fun getEntryByTitle(title: String): JournalEntry? =
        _journalList.find { it.entryTitle == title }

    /**
     * 获取特定索引的日志条目
     */
    fun getEntryByIndex(index: Int): JournalEntry? {
        return if (index >= 0 && index < _journalList.size) {
            _journalList[index]
        } else {
            null
        }
    }

    /**
     * 刷新当前客户端的日志数据
     */
    fun refresh() {
        if (currentClientId.isNotEmpty() && currentCounsellorId.isNotEmpty()) {
            loadForClient(currentClientId, currentCounsellorId)
        }
    }

    /**
     * 清除错误状态
     */
    fun clearError() {
        error.value = null
    }

    /**
     * 公共方法：强制加载测试数据用于演示
     */
    fun loadTestDataForDemo() {
        loadTestData("ALICE")
        isLoading.value = false
        error.value = null
    }
}
