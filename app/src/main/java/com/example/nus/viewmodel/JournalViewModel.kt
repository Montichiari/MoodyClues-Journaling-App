package com.example.nus.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.nus.model.JournalEntry
import java.time.LocalDateTime

class JournalViewModel : ViewModel() {

    // Backing list of model.JournalEntry
    private val _journalList = mutableStateListOf<JournalEntry>()
    val journalList: List<JournalEntry> get() = _journalList

    /**
     * Load entries for a specific client (replace with real API later).
     */
    fun loadForClient(clientId: String) {
        _journalList.clear()
        val now = LocalDateTime.now()
        _journalList.addAll(
            listOf(
                JournalEntry(
                    user = clientId,
                    entryTitle = "Those Goddamn Ducks…",
                    entryText = "I can’t stop worrying about the ducks.",
                    date = now
                ),
                JournalEntry(
                    user = clientId,
                    entryTitle = "Gabagool",
                    entryText = "Had some amazing gabagool today.",
                    date = now.minusDays(1)
                )
            )
        )
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
}
