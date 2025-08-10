// com/example/nus/ui/screens/JournalScreen.kt
package com.example.nus.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nus.model.JournalEntry
import com.example.nus.ui.navigation.Screen
import com.example.nus.viewmodel.JournalViewModel
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JournalScreen(
    viewModel: JournalViewModel,
    clientUserId: String,
    navController: NavController
) {
    val isLoading = viewModel.isLoading.value
    val error = viewModel.error.value
    val journalList: List<JournalEntry> = viewModel.journalList
    val responses = viewModel.responses

    androidx.compose.runtime.LaunchedEffect(clientUserId) {
        viewModel.loadForClient(clientUserId)
    }

    when {
        isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: $error")
        }
        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(journalList) { index, entry ->
                    val entryId = responses.getOrNull(index)?.id ?: return@itemsIndexed
                    JournalItem(entry = entry) {
                        navController.navigate(Screen.JournalDetail.createRoute(entryId))
                    }
                }
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

