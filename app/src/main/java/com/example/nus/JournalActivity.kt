package com.example.nus

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nus.model.JournalEntry
import java.time.LocalDateTime

class JournalActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hardcoded test data
        val journalList = listOf(
            JournalEntry(user = "user1", entryTitle = "Those goddamn ducks..."),
            JournalEntry(user = "user2", entryTitle = "Gabagool"),
            JournalEntry(user = "user3", entryTitle = "Nostradamus...")
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

@RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JournalCard(entry: JournalEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Convert LocalDateTime to readable string
            Text(
                text = entry.date.toLocalDate().toString(),
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Show the entry title instead of mood
            Text(
                text = entry.entryTitle,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

