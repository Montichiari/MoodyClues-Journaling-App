package com.example.nus.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Brightness3
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nus.model.JournalEntry
import com.example.nus.viewmodel.JournalDetailViewModel
import com.example.nus.viewmodel.LifestyleViewModel
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun JournalDetailContent(entry: JournalEntry, lifestyleViewModel: LifestyleViewModel? = null) {
    val dateStr = entry.date.format(DateTimeFormatter.ofPattern("yyyy/M/d HH:mm:ss"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        // Header Section
        Text(
            text = "Your Journal",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = entry.entryTitle,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Date
        Text(
            text = dateStr,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Mood Section
        MoodSection(entry = entry)

        Spacer(modifier = Modifier.height(16.dp))

        // Habits Section
        HabitsSection(entry = entry, lifestyleViewModel = lifestyleViewModel)

        Spacer(modifier = Modifier.height(24.dp))

        // Journal Content Section
        JournalContentSection(entry = entry)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JournalDetailScreen(
    viewModel: JournalDetailViewModel,
    lifestyleViewModel: LifestyleViewModel? = null,
    onRetry: () -> Unit = {}
) {
    val journalEntry by viewModel.journalEntry
    val isLoading by viewModel.isLoading
    val error by viewModel.error

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            error != null -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error loading journal entry",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = error ?: "Unknown error",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.clearError()
                            onRetry()
                        }
                    ) {
                        Text("Retry")
                    }
                }
            }
            journalEntry != null -> {
                JournalDetailContent(entry = journalEntry!!, lifestyleViewModel = lifestyleViewModel)
            }
            else -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Journal entry not found",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun MoodSection(entry: JournalEntry) {
    // 判断是否有多时段心情数据
    val hasMultipleMoods = entry.moodMorning > 0 || entry.moodAfternoon > 0 || entry.moodEvening > 0

    if (hasMultipleMoods) {
        // 显示多时段心情
        Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            if (entry.moodMorning > 0) {
                MoodItem(
                    label = "Morning",
                    mood = entry.moodMorning,
                    icon = Icons.Default.WbSunny
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (entry.moodAfternoon > 0) {
                MoodItem(
                    label = "Afternoon",
                    mood = entry.moodAfternoon,
                    icon = Icons.Default.WbSunny
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (entry.moodEvening > 0) {
                MoodItem(
                    label = "Evening",
                    mood = entry.moodEvening,
                    icon = Icons.Default.Brightness3
                )
            }
        }
    } else if (entry.mood > 0) {
        // 显示单一心情
        MoodItem(
            label = "Mood",
            mood = entry.mood,
            icon = Icons.Default.Mood
        )
    }
}

@Composable
private fun MoodItem(
    label: String,
    mood: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    val moodText = when {
        mood >= 8 -> "Excellent"
        mood >= 6 -> "Good"
        mood >= 4 -> "Neutral"
        mood >= 2 -> "Poor"
        else -> "Very Poor"
    }

    Text(
        text = "$label: $moodText",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun HabitsSection(entry: JournalEntry, lifestyleViewModel: LifestyleViewModel?) {
    // 只使用LifestyleViewModel中的真实用户数据
    val lifestyleEntries = lifestyleViewModel?.lifestyleEntries ?: emptyList()
    val todayEntry = lifestyleEntries.find { it.date == java.time.LocalDate.now() }

    // 调试信息
    println("HabitsSection: Found ${lifestyleEntries.size} lifestyle entries")
    println("HabitsSection: Today's entry: $todayEntry")

    // 只有当找到今天的真实数据时才显示
    todayEntry?.let { realEntry ->
        val sleepHours = realEntry.sleepHours.toDouble()
        val waterLitres = realEntry.waterLitres
        val workHours = realEntry.workHours

        val hasHabitsData = sleepHours > 0 || waterLitres > 0 || workHours > 0

        // 调试信息
        println("HabitsSection: sleep=$sleepHours, water=$waterLitres, workHours=$workHours, hasHabitsData=$hasHabitsData")
        println("HabitsSection: Using real user data")

        if (hasHabitsData) {
            Column(
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                if (sleepHours > 0) {
                    HabitsItem(
                        label = "Sleep hours",
                        value = sleepHours.toString(),
                        icon = Icons.Default.Bedtime
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                if (waterLitres > 0) {
                    HabitsItem(
                        label = "Water (liters)",
                        value = waterLitres.toString(),
                        icon = Icons.Default.WaterDrop
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                if (workHours > 0) {
                    HabitsItem(
                        label = "Work hours",
                        value = workHours.toString(),
                        icon = Icons.Default.Work
                    )
                }
            }
        }
    } ?: run {
        // 如果没有今天的数据，显示提示信息
        println("HabitsSection: No real user data found for today")
        Text(
            text = "No lifestyle data recorded for today",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun HabitsItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Text(
        text = "$label: $value",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun JournalContentSection(entry: JournalEntry) {
    if (entry.entryText.isNotBlank()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = entry.entryText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                )
            }
        }
    }
}
