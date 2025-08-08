package com.example.nus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nus.model.MoodType
import com.example.nus.model.TimeOfDay
import com.example.nus.viewmodel.MoodViewModel
import java.time.LocalDate

@Composable
fun MoodScreen(
    viewModel: MoodViewModel,
    onNavigateToFeel: () -> Unit = {},
    userId: String = "" // 从登录响应传入的用户ID
) {
    var journalTitle by remember { mutableStateOf("") }
    var journalContent by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf<MoodType?>(null) }

    // 设置用户ID并测试ML模型连接
    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            viewModel.setUserId(userId)
            // 测试ML模型连接
            viewModel.testMLModelConnection()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Main content card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Date display - using current date
                val currentDate = LocalDate.now()
                val dateText = "Today is ${currentDate.dayOfMonth}${getDaySuffix(currentDate.dayOfMonth)} ${currentDate.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${currentDate.year}."

                Text(
                    text = dateText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Write into your journal, and log how you feel right now.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // How do you feel section
                Text(
                    text = "How do you feel?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Mood selection buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    MoodButton(
                        text = "Great",
                        mood = MoodType.VERY_GOOD,
                        isSelected = selectedMood == MoodType.VERY_GOOD,
                        onSelect = { selectedMood = MoodType.VERY_GOOD },
                        modifier = Modifier.weight(1f)
                    )
                    MoodButton(
                        text = "Good",
                        mood = MoodType.GOOD,
                        isSelected = selectedMood == MoodType.GOOD,
                        onSelect = { selectedMood = MoodType.GOOD },
                        modifier = Modifier.weight(1f)
                    )
                    MoodButton(
                        text = "Okay",
                        mood = MoodType.NEUTRAL,
                        isSelected = selectedMood == MoodType.NEUTRAL,
                        onSelect = { selectedMood = MoodType.NEUTRAL },
                        modifier = Modifier.weight(1f)
                    )
                    MoodButton(
                        text = "Bad",
                        mood = MoodType.BAD,
                        isSelected = selectedMood == MoodType.BAD,
                        onSelect = { selectedMood = MoodType.BAD },
                        modifier = Modifier.weight(1f)
                    )
                    MoodButton(
                        text = "Awful",
                        mood = MoodType.VERY_BAD,
                        isSelected = selectedMood == MoodType.VERY_BAD,
                        onSelect = { selectedMood = MoodType.VERY_BAD },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Journal title prompt
                Text(
                    text = "Start with a title to remember this day.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Title input field
                OutlinedTextField(
                    value = journalTitle,
                    onValueChange = { journalTitle = it },
                    placeholder = { Text("Today, I...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp)
                )

                // Journal content input field
                OutlinedTextField(
                    value = journalContent,
                    onValueChange = { journalContent = it },
                    placeholder = { Text("Write about your day...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 10
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Error Message
                viewModel.error.value?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Save button
                Button(
                    onClick = {
                        selectedMood?.let { mood ->
                            viewModel.submitJournalEntryWithMLPrediction(
                                mood = mood,
                                entryTitle = journalTitle,
                                entryText = journalContent,
                                onSuccess = {
                                    // 清空表单
                                    journalTitle = ""
                                    journalContent = ""
                                    selectedMood = null
                                    onNavigateToFeel()
                                },
                                onError = { error ->
                                    viewModel.error.value = error
                                }
                            )
                        } ?: run {
                            viewModel.error.value = "Please select your mood first"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = !viewModel.isLoading.value
                ) {
                    if (viewModel.isLoading.value) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            text = "Save & Continue",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun MoodButton(
    text: String,
    mood: MoodType,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        isSelected && mood == MoodType.VERY_BAD -> Color(0xFFDC3545)
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    }

    val textColor = when {
        isSelected -> Color.White
        else -> MaterialTheme.colorScheme.onSurface
    }

    Button(
        onClick = onSelect,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.height(36.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp, vertical = 0.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            maxLines = 1,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
    }
}

// Helper function to get day suffix (1st, 2nd, 3rd, etc.)
fun getDaySuffix(day: Int): String {
    return when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }
}

