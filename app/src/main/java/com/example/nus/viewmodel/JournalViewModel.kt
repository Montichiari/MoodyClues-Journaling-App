package com.example.nus.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nus.api.ApiClient
import com.example.nus.model.Emotion
import com.example.nus.model.JournalEntry
import com.example.nus.model.JournalEntryResponse
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException

class JournalViewModel : ViewModel() {

    // --- Network/UI state ---
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    // --- List state ---
    // Keep responses so we have stable IDs for navigation
    private val _responses = mutableStateListOf<JournalEntryResponse>()
    val responses: List<JournalEntryResponse> get() = _responses

    // Keep your original domain list so existing UI code that expects JournalEntry still works
    private val _journalList = mutableStateListOf<JournalEntry>()
    val journalList: List<JournalEntry> get() = _journalList

    // --- Detail state ---
    val selectedEntry = mutableStateOf<JournalEntry?>(null)

    /**
     * Load entries for a specific client (counsellor flow) from the backend.
     * Replaces the previous hardcoded dummy entries.
     */
    fun loadForClient(clientId: String) {
        if (clientId.isBlank()) {
            error.value = "Client id missing"
            return
        }
        isLoading.value = true
        error.value = null

        viewModelScope.launch {
            try {
                val res = ApiClient.journalApiService.getAllJournalEntries(clientId)
                if (res.isSuccessful) {
                    val list = res.body().orEmpty()

                    // store raw responses (for IDs)
                    _responses.clear()
                    _responses.addAll(list)

                    // also expose your original domain objects for existing UI
                    _journalList.clear()
                    _journalList.addAll(list.map { it.toDomain() })
                } else {
                    error.value = "Load failed: ${res.code()} ${res.message()}"
                }
            } catch (e: Exception) {
                error.value = "Network error: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    /**
     * Fetch one entry by its ID for the detail screen.
     */
    fun loadById(entryId: String) {
        if (entryId.isBlank()) {
            error.value = "Entry id missing"
            return
        }
        isLoading.value = true
        error.value = null

        viewModelScope.launch {
            try {
                val res = ApiClient.journalApiService.getJournalEntryById(entryId)
                if (res.isSuccessful && res.body() != null) {
                    selectedEntry.value = res.body()!!.toDomain()
                } else {
                    error.value = "Load failed: ${res.code()} ${res.message()}"
                }
            } catch (e: Exception) {
                error.value = "Network error: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    /**
     * Archive an entry (from detail or list). Caller can refresh or navigate on success.
     */
    fun archive(entryId: String, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val res = ApiClient.journalApiService.archiveJournalEntry(entryId)
                if (res.isSuccessful) {
                    onDone()
                } else {
                    error.value = "Archive failed: ${res.code()} ${res.message()}"
                }
            } catch (e: Exception) {
                error.value = "Network error: ${e.message}"
            }
        }
    }

    /**
     * Add a new entry locally (kept for parity with your previous code; optional).
     * This does NOT call the API.
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
     * Look up a domain entry by title (kept for backward compatibility with your UI).
     */
    fun getEntryByTitle(title: String): JournalEntry? =
        _journalList.find { it.entryTitle == title }

    /**
     * Helper to get the backend ID for a row if your UI still navigates using the title.
     * Prefer passing the ID directly from the list item click if possible.
     */
    fun getEntryIdByTitle(title: String): String? =
        _responses.find { it.entryTitle == title }?.id

    // ---------------- Helpers / Mappings ----------------

    // Convert API response -> your domain JournalEntry (inline, no separate mapper file)
    private fun JournalEntryResponse.toDomain(): JournalEntry {
        return JournalEntry(
            user = this.userId,
            entryTitle = this.entryTitle,
            entryText = this.entryText,
            emotions = this.emotions.map { label ->
                Emotion(
                    id = label.lowercase(),          // or keep server id if you add it later
                    emotionLabel = label,
                    iconAddress = emotionIconFor(label) // stub; customize as needed
                )
            },
            mood = this.mood,
            lastSavedAt = parseIsoDate(this.createdAt),
            createdAt = parseIsoDate(this.createdAt),
            date = parseIsoDate(this.createdAt)
        )
    }

    private fun emotionIconFor(label: String): String {
        return when (label.lowercase()) {
            "happy" -> "ic_emotion_happy"
            "sad"   -> "ic_emotion_sad"
            "angry" -> "ic_emotion_angry"
            else    -> "" // no icon yet
        }
    }

    private fun parseIsoDate(iso: String?): LocalDateTime {
        if (iso.isNullOrBlank()) return LocalDateTime.now()
        return try {
            // Handles ISO-8601 like "2025-08-10T12:34:56Z" or with offsets
            OffsetDateTime.parse(iso).toLocalDateTime()
        } catch (_: DateTimeParseException) {
            LocalDateTime.now()
        }
    }
}
