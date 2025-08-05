package com.example.nus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nus.model.JournalEntry

// shows a list of journal entries
@Composable
fun JournalScreen() {
    // Hardcoded list for now
    val journalList = listOf(
        JournalEntry("21/7/2025", "Those goddamn ducks..."),
        JournalEntry("22/7/2025", "Gabagool"),
        JournalEntry("24/7/2025", "Nostradamus...")
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        items(journalList) { entry ->
            JournalItem(entry)
        }
    }
}

@Composable
fun JournalItem(entry: JournalEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = entry.date, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = entry.content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
