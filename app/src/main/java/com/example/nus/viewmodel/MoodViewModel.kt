package com.example.nus.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nus.api.ApiClient
import com.example.nus.model.JournalEntryRequest
import com.example.nus.model.MLModelRequest
import com.example.nus.model.MoodEntry
import com.example.nus.model.MoodType
import com.example.nus.model.TimeOfDay
import kotlinx.coroutines.launch
import java.time.LocalDate

class MoodViewModel : ViewModel() {
    private val _moodEntries = mutableStateListOf<MoodEntry>()
    val moodEntries: List<MoodEntry> get() = _moodEntries

    val selectedDate = mutableStateOf(LocalDate.now())
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)
    val submitSuccess = mutableStateOf(false)

    // 当前用户ID（从登录响应获取）
    var currentUserId: String = ""

    // 存储ML模型返回的情感
    val predictedEmotions = mutableStateOf<List<String>>(emptyList())

    // ML模型是否可用
    val isMLModelAvailable = mutableStateOf(true)
    
    // 提交日记条目到后端
    fun submitJournalEntry(
        mood: MoodType,
        entryTitle: String,
        entryText: String,
        emotions: List<String> = emptyList(),
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (currentUserId.isEmpty()) {
            onError("User not logged in")
            return
        }

        if (entryTitle.isBlank()) {
            onError("Please enter a title for your journal entry")
            return
        }

        isLoading.value = true
        error.value = null
        submitSuccess.value = false

        viewModelScope.launch {
            try {
                val journalRequest = JournalEntryRequest(
                    userId = currentUserId,
                    mood = mood.value,
                    entryTitle = entryTitle,
                    entryText = entryText,
                    emotions = emotions
                )

                println("Submitting journal entry: userId=$currentUserId, mood=${mood.value}, title=$entryTitle")
                val response = ApiClient.journalApiService.submitJournalEntry(journalRequest)

                if (response.isSuccessful) {
                    // 添加到本地列表
                    addMoodEntryLocal(mood, TimeOfDay.MORNING, entryTitle, entryText, emotions)
                    submitSuccess.value = true
                    onSuccess()
                    println("Journal entry submitted successfully")
                } else {
                    val errorMessage = "Failed to submit journal entry: ${response.code()} - ${response.message()}"
                    println("Submit error: $errorMessage")
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = "Network error: ${e.message}"
                println("Submit exception: $errorMessage")
                onError(errorMessage)
            } finally {
                isLoading.value = false
            }
        }
    }

    // 新的提交方法，先调用ML模型，再调用Spring Boot
    fun submitJournalEntryWithMLPrediction(
        mood: MoodType,
        entryTitle: String,
        entryText: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (currentUserId.isEmpty()) {
            onError("User not logged in")
            return
        }

        if (entryTitle.isBlank()) {
            onError("Please enter a title for your journal entry")
            return
        }

        if (entryText.isBlank()) {
            onError("Please enter some content for emotion analysis")
            return
        }

        isLoading.value = true
        error.value = null
        submitSuccess.value = false

        viewModelScope.launch {
            try {
                // 第一步：尝试调用ML模型API进行情感分析
                println("Calling ML model for emotion prediction...")
                val mlRequest = MLModelRequest(text = entryText)

                var emotions: List<String> = emptyList()
                var mlModelSuccess = false

                try {
                    val mlResponse = ApiClient.mlModelApiService.predictEmotions(mlRequest)

                    if (mlResponse.isSuccessful) {
                        emotions = mlResponse.body()?.emotions ?: emptyList()
                        predictedEmotions.value = emotions
                        mlModelSuccess = true
                        println("ML model predicted emotions: $emotions")
                    } else {
                        println("ML model API error: ${mlResponse.code()} - ${mlResponse.message()}")
                        // 打印响应体以便调试
                        val errorBody = mlResponse.errorBody()?.string()
                        println("ML model error response body: $errorBody")
                        emotions = emptyList()
                        println("Using empty emotions due to ML model API error")
                    }
                } catch (mlException: Exception) {
                    println("ML model network error: ${mlException.javaClass.simpleName}: ${mlException.message}")
                    mlException.printStackTrace()
                    isMLModelAvailable.value = false
                    // 继续执行，使用空的emotions列表
                    emotions = emptyList()
                    println("Using empty emotions due to ML model failure")
                }

                // 第二步：调用Spring Boot后端提交日记（无论ML模型是否成功）

                val journalRequest = JournalEntryRequest(
                    userId = currentUserId,
                    mood = mood.value,
                    entryTitle = entryTitle,
                    entryText = entryText,
                    emotions = emotions
                )

                println("Submitting journal entry to backend...")
                val backendResponse = ApiClient.journalApiService.submitJournalEntry(journalRequest)

                if (backendResponse.isSuccessful) {
                    // 读取响应体内容
                    val responseText = backendResponse.body()?.string() ?: "Success"
                    println("Backend response: $responseText")

                    // 添加到本地列表
                    addMoodEntryLocal(mood, TimeOfDay.MORNING, entryTitle, entryText, emotions)
                    submitSuccess.value = true

                    if (!mlModelSuccess) {
                        // 如果ML模型失败，设置空情感用于显示
                        predictedEmotions.value = emptyList()
                    }

                    onSuccess()
                    println("Journal entry submitted successfully with emotions: $emotions")
                } else {
                    val errorMessage = "Failed to submit to backend: ${backendResponse.code()} - ${backendResponse.message()}"
                    println("Backend error: $errorMessage")
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = "Network error: ${e.message}"
                println("Exception: $errorMessage")
                onError(errorMessage)
            } finally {
                isLoading.value = false
            }
        }
    }

    // 测试ML模型连接的方法
    fun testMLModelConnection() {
        viewModelScope.launch {
            try {
                println("Testing ML model connection...")
                val testRequest = MLModelRequest(text = "test")
                val response = ApiClient.mlModelApiService.predictEmotions(testRequest)

                if (response.isSuccessful) {
                    println("ML model connection successful: ${response.body()}")
                } else {
                    println("ML model connection failed: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                println("ML model connection error: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    // 本地添加心情条目（保持原有功能）
    private fun addMoodEntryLocal(
        mood: MoodType,
        timeOfDay: TimeOfDay,
        entryTitle: String = "",
        entryText: String = "",
        emotions: List<String> = emptyList()
    ) {
        val existingEntryIndex = _moodEntries.indexOfFirst {
            it.timeOfDay == timeOfDay && it.date == selectedDate.value
        }

        if (existingEntryIndex >= 0) {
            _moodEntries[existingEntryIndex] = MoodEntry(
                id = _moodEntries[existingEntryIndex].id,
                mood = mood,
                timeOfDay = timeOfDay,
                date = selectedDate.value,
                entryTitle = entryTitle,
                entryText = entryText,
                emotions = emotions,
                userId = currentUserId
            )
        } else {
            _moodEntries.add(
                MoodEntry(
                    mood = mood,
                    timeOfDay = timeOfDay,
                    date = selectedDate.value,
                    entryTitle = entryTitle,
                    entryText = entryText,
                    emotions = emotions,
                    userId = currentUserId
                )
            )
        }
    }

    // 兼容性方法（保持原有API）
    fun addMoodEntry(mood: MoodType, timeOfDay: TimeOfDay, notes: String = "") {
        addMoodEntryLocal(mood, timeOfDay, notes, notes)
    }
    
    // 获取所有日记条目
    fun loadAllJournalEntries(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (currentUserId.isEmpty()) {
            onError("User not logged in")
            return
        }

        isLoading.value = true
        error.value = null

        viewModelScope.launch {
            try {
                val response = ApiClient.journalApiService.getAllJournalEntries(currentUserId)

                if (response.isSuccessful) {
                    response.body()?.let { journalEntries ->
                        _moodEntries.clear()
                        _moodEntries.addAll(journalEntries.map { MoodEntry.fromJournalEntryResponse(it) })
                        onSuccess()
                        println("Loaded ${journalEntries.size} journal entries")
                    } ?: run {
                        onError("Empty response from server")
                    }
                } else {
                    val errorMessage = "Failed to load journal entries: ${response.code()} - ${response.message()}"
                    println("Load error: $errorMessage")
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = "Network error: ${e.message}"
                println("Load exception: $errorMessage")
                onError(errorMessage)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun getMoodForTimeOfDay(timeOfDay: TimeOfDay): MoodType? {
        return _moodEntries.find {
            it.timeOfDay == timeOfDay && it.date == selectedDate.value
        }?.mood
    }

    fun changeSelectedDate(date: LocalDate) {
        selectedDate.value = date
    }

    fun clearError() {
        error.value = null
    }

    fun setUserId(userId: String) {
        currentUserId = userId
    }
} 