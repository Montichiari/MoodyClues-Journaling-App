package com.example.nus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nus.model.JournalEntry

class JournalActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hardcoded test data
        val journalList = listOf(
            JournalEntry("21/7/2025", "Those goddamn ducks..."),
            JournalEntry("22/7/2025", "Gabagool"),
            JournalEntry("24/7/2025", "Nostradamus...")
        )

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JournalScreen(journalList)
                }
            }
        }
    }
}

@Composable
fun JournalScreen(journalList: List<JournalEntry>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(journalList) { entry ->
            JournalCard(entry)
        }
    }
}

@Composable
fun JournalCard(entry: JournalEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = entry.date, style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = entry.content, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
