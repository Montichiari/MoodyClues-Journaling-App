package com.example.nus.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nus.model.JournalEntry

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JournalScreen(navController: NavController) {
    // Hardcoded list of journal entries for testing
    val journalList = listOf(
        JournalEntry("21/7/2025", "Those goddamn ducks..."),
        JournalEntry("22/7/2025", "Gabagool"),
        JournalEntry("24/7/2025", "Nostradamus...")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(journalList) { entry ->
            JournalItem(entry) {
                navController.navigate("journalDetail")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JournalItem(entry: JournalEntry, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Show formatted date
            Text(
                text = entry.date.toLocalDate().toString(),
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Show journal title or text
            Text(
                text = entry.entryTitle,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


