package com.example.nus.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nus.api.ApiClient
import com.example.nus.model.JournalEntry
import kotlinx.coroutines.launch

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
}
