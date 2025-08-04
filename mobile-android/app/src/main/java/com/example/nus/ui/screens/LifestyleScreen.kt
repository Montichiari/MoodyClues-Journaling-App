package com.example.nus.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nus.viewmodel.LifestyleViewModel
import kotlin.math.roundToInt

@Composable
fun LifestyleScreen(viewModel: LifestyleViewModel) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        DateSelector(
            selectedDate = viewModel.selectedDate.value,
            onDateSelected = { viewModel.loadEntryForDate(it) }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Lifestyle Tracking",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Sleep Hours
                Text(
                    text = "Sleep Hours",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Slider(
                        value = viewModel.sleepHours.value,
                        onValueChange = { viewModel.sleepHours.value = it },
                        valueRange = 0f..12f,
                        steps = 23,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${viewModel.sleepHours.value.roundToInt()} hours",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Water Glasses
                Text(
                    text = "Water Intake (glasses)",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Slider(
                        value = viewModel.waterGlasses.value.toFloat(),
                        onValueChange = { viewModel.waterGlasses.value = it.roundToInt() },
                        valueRange = 0f..10f,
                        steps = 10,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${viewModel.waterGlasses.value} glasses",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Overtime
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = viewModel.didOvertime.value,
                        onCheckedChange = { viewModel.didOvertime.value = it }
                    )
                    Text(
                        text = "Did you work overtime today?",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Exercise Minutes
                Text(
                    text = "Exercise (minutes)",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Slider(
                        value = viewModel.exerciseMinutes.value.toFloat(),
                        onValueChange = { viewModel.exerciseMinutes.value = it.roundToInt() },
                        valueRange = 0f..120f,
                        steps = 12,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${viewModel.exerciseMinutes.value} minutes",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Stress Level
                Text(
                    text = "Stress Level (0-10)",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Slider(
                        value = viewModel.stressLevel.value.toFloat(),
                        onValueChange = { viewModel.stressLevel.value = it.roundToInt() },
                        valueRange = 0f..10f,
                        steps = 10,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${viewModel.stressLevel.value}",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Social Interaction
                Text(
                    text = "Social Interaction (hours)",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Slider(
                        value = viewModel.socialInteractionHours.value,
                        onValueChange = { viewModel.socialInteractionHours.value = it },
                        valueRange = 0f..10f,
                        steps = 20,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${viewModel.socialInteractionHours.value} hours",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Notes
                Text(
                    text = "Notes",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                OutlinedTextField(
                    value = viewModel.notes.value,
                    onValueChange = { viewModel.notes.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Add notes for today...") }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = { viewModel.saveLifestyleEntry() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }
            }
        }
    }
} 