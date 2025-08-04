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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun MoodScreen(viewModel: MoodViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        DateSelector(
            selectedDate = viewModel.selectedDate.value,
            onDateSelected = { viewModel.changeSelectedDate(it) }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Today's Mood",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        MoodSection(
            title = "Morning",
            timeOfDay = TimeOfDay.MORNING,
            selectedMood = viewModel.getMoodForTimeOfDay(TimeOfDay.MORNING),
            onMoodSelected = { mood -> 
                viewModel.addMoodEntry(mood, TimeOfDay.MORNING)
            }
        )
        
        MoodSection(
            title = "Afternoon",
            timeOfDay = TimeOfDay.AFTERNOON,
            selectedMood = viewModel.getMoodForTimeOfDay(TimeOfDay.AFTERNOON),
            onMoodSelected = { mood -> 
                viewModel.addMoodEntry(mood, TimeOfDay.AFTERNOON)
            }
        )
        
        MoodSection(
            title = "Evening",
            timeOfDay = TimeOfDay.EVENING,
            selectedMood = viewModel.getMoodForTimeOfDay(TimeOfDay.EVENING),
            onMoodSelected = { mood -> 
                viewModel.addMoodEntry(mood, TimeOfDay.EVENING)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            onDateSelected(selectedDate.minusDays(1))
        }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Previous Day")
        }
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { showDatePicker = true }
        ) {
            Text(
                text = selectedDate.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "Select Date"
            )
        }
        
        IconButton(onClick = {
            onDateSelected(selectedDate.plusDays(1))
        }) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Next Day")
        }
    }
    
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
        
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val newDate = Instant
                            .ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onDateSelected(newDate)
                    }
                    showDatePicker = false
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun MoodSection(
    title: String,
    timeOfDay: TimeOfDay,
    selectedMood: MoodType?,
    onMoodSelected: (MoodType) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MoodOption(
                    mood = MoodType.VERY_GOOD,
                    label = "Very Good",
                    isSelected = selectedMood == MoodType.VERY_GOOD,
                    onSelect = { onMoodSelected(MoodType.VERY_GOOD) }
                )
                
                MoodOption(
                    mood = MoodType.GOOD,
                    label = "Good",
                    isSelected = selectedMood == MoodType.GOOD,
                    onSelect = { onMoodSelected(MoodType.GOOD) }
                )
                
                MoodOption(
                    mood = MoodType.NEUTRAL,
                    label = "Neutral",
                    isSelected = selectedMood == MoodType.NEUTRAL,
                    onSelect = { onMoodSelected(MoodType.NEUTRAL) }
                )
                
                MoodOption(
                    mood = MoodType.BAD,
                    label = "Bad",
                    isSelected = selectedMood == MoodType.BAD,
                    onSelect = { onMoodSelected(MoodType.BAD) }
                )
                
                MoodOption(
                    mood = MoodType.VERY_BAD,
                    label = "Very Bad",
                    isSelected = selectedMood == MoodType.VERY_BAD,
                    onSelect = { onMoodSelected(MoodType.VERY_BAD) }
                )
            }
        }
    }
}

@Composable
fun MoodOption(
    mood: MoodType,
    label: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val color = when (mood) {
        MoodType.VERY_GOOD -> Color(0xFF4CAF50)
        MoodType.GOOD -> Color(0xFF8BC34A)
        MoodType.NEUTRAL -> Color(0xFFFFC107)
        MoodType.BAD -> Color(0xFFFF9800)
        MoodType.VERY_BAD -> Color(0xFFF44336)
    }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onSelect() }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(if (isSelected) color else Color.LightGray.copy(alpha = 0.3f))
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) color.copy(alpha = 0.7f) else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            val emoji = when (mood) {
                MoodType.VERY_GOOD -> "😄"
                MoodType.GOOD -> "🙂"
                MoodType.NEUTRAL -> "😐"
                MoodType.BAD -> "🙁"
                MoodType.VERY_BAD -> "😢"
            }
            Text(
                text = emoji,
                fontSize = 24.sp
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = label,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
} 