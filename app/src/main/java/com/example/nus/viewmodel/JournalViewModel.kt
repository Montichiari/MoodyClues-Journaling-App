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
    private val _responses = mutableStateListOf<JournalEntryResponse>()
    val responses: List<JournalEntryResponse> get() = _responses

    private val _journalList = mutableStateListOf<JournalEntry>()
    val journalList: List<JournalEntry> get() = _journalList

    // --- Detail state ---
    val selectedEntry = mutableStateOf<JournalEntry?>(null)

    /**
     * Load entries for a specific client (counsellor flow) from the backend.
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
                // ---- DEBUG LINE (Step 4) ----
                println("JournalVM: loading entries for clientId='$clientId'")

                val res = ApiClient.journalApiService.getAllJournalEntries(clientId)

                // More visibility on what the server responded with
                println("JournalVM: GET /api/journal/all -> HTTP ${res.code()} ${res.message()}")

                if (res.isSuccessful) {
                    val list = res.body().orEmpty()
                    println("JournalVM: received ${list.size} entries")

                    _responses.clear()
                    _responses.addAll(list)

                    _journalList.clear()
                    _journalList.addAll(list.map { it.toDomain() })
                } else {
                    error.value = "Load failed: ${res.code()} ${res.message()}"
                }
            } catch (e: Exception) {
                error.value = "Network error: ${e.message}"
                println("JournalVM: exception while loading list -> ${e.message}")
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
                // ---- DEBUG LINE (helpful too) ----
                println("JournalVM: loading entry by id='$entryId'")

                val res = ApiClient.journalApiService.getJournalEntryById(entryId)
                println("JournalVM: GET /api/journal/{entryId} -> HTTP ${res.code()} ${res.message()}")

                if (res.isSuccessful && res.body() != null) {
                    selectedEntry.value = res.body()!!.toDomain()
                } else {
                    error.value = "Load failed: ${res.code()} ${res.message()}"
                }
            } catch (e: Exception) {
                error.value = "Network error: ${e.message}"
                println("JournalVM: exception while loading detail -> ${e.message}")
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
                println("JournalVM: archiving entryId='$entryId'")
                val res = ApiClient.journalApiService.archiveJournalEntry(entryId)
                println("JournalVM: PUT /api/journal/{entryId}/archive -> HTTP ${res.code()} ${res.message()}")
                if (res.isSuccessful) {
                    onDone()
                } else {
                    error.value = "Archive failed: ${res.code()} ${res.message()}"
                }
            } catch (e: Exception) {
                error.value = "Network error: ${e.message}"
                println("JournalVM: exception while archiving -> ${e.message}")
            }
        }
    }

    // ----- Local helpers kept from your previous version -----

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

    fun getEntryByTitle(title: String): JournalEntry? =
        _journalList.find { it.entryTitle == title }

    fun getEntryIdByTitle(title: String): String? =
        _responses.find { it.entryTitle == title }?.id

    // ---------------- Helpers / Mappings ----------------

    private fun JournalEntryResponse.toDomain(): JournalEntry {
        return JournalEntry(
            user = this.userId,
            entryTitle = this.entryTitle,
            entryText = this.entryText,
            emotions = this.emotions.map { label ->
                Emotion(
                    id = label.lowercase(),
                    emotionLabel = label,
                    iconAddress = emotionIconFor(label)
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
            else    -> ""
        }
    }

    private fun parseIsoDate(iso: String?): LocalDateTime {
        if (iso.isNullOrBlank()) return LocalDateTime.now()
        return try {
            OffsetDateTime.parse(iso).toLocalDateTime()
        } catch (_: DateTimeParseException) {
            LocalDateTime.now()
        }
    }
}
