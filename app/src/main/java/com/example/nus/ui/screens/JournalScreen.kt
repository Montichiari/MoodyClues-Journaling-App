package com.example.nus.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nus.model.JournalEntry
import com.example.nus.ui.navigation.Screen
import java.time.format.DateTimeFormatter




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JournalScreen(
    journalList: List<JournalEntry>,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(journalList) { index, entry ->
            JournalItem(entry = entry) {
                // Navigate using the index to match route: journalDetail/{entryIndex}
                navController.navigate(Screen.JournalDetail.createRoute(index))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun JournalItem(
    entry: JournalEntry,
    onClick: () -> Unit
) {
    val formattedDate = entry.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = formattedDate, style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = entry.entryTitle, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
