package com.example.nus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun JournalDetailScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Tony Soprano's Journal",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Journal Entry Title
        Text(
            text = "Those goddamn ducks...",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Date
        Text(
            text = "21st July 2025",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mood Rows
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Morning: Bad")
            Text("Afternoon: Very Bad")
            Text("Evening: Very Bad")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Health Info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Sleep: 4 Hours")
            Text("Water: 2 Litres")
            Text("Work: 6 Hours")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Feelings Section
        Text("You felt:")
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("😠") // Replace with Icon if needed
                Text("Angry")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("😢")
                Text("Sad")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Journal Entry Content
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Text(
                text = "You're right -- that's what I'm full of dread about, that I'm going to lose my family .. Just like I lost the ducks. It's always with me.",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
