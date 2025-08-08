package com.example.nus.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nus.viewmodel.LifestyleViewModel
import java.time.LocalDate

@Composable
fun LifestyleScreen(
    viewModel: LifestyleViewModel,
    userId: String = "", // 从导航传入的用户ID
    onNavigateToLifestyleLogged: () -> Unit = {}
) {
    var sleepHours by remember { mutableStateOf("") }
    var waterLitres by remember { mutableStateOf("") }
    var workHours by remember { mutableStateOf("") }

    // 设置用户ID
    LaunchedEffect(userId) {
        println("LifestyleScreen: Received userId = '$userId'")
        if (userId.isNotEmpty()) {
            viewModel.setUserId(userId)
            println("LifestyleScreen: Set userId in viewModel = '$userId'")
        } else {
            println("LifestyleScreen: WARNING - userId is empty!")
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
                    text = "Log your lifestyle choices for the day.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Sleep question
                Text(
                    text = "How much did you sleep the previous night?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = sleepHours,
                    onValueChange = { sleepHours = it },
                    placeholder = { Text("8 hours") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                // Water question
                Text(
                    text = "How much water did you drink today (litres)?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = waterLitres,
                    onValueChange = { waterLitres = it },
                    placeholder = { Text("2 litres") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                // Work hours question
                Text(
                    text = "How many hours did I work today?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = workHours,
                    onValueChange = { workHours = it },
                    placeholder = { Text("8 hours") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
                        // 验证并转换输入
                        val sleepValue = sleepHours.toDoubleOrNull()
                        val waterValue = waterLitres.toDoubleOrNull()
                        val workValue = workHours.toDoubleOrNull()

                        when {
                            sleepValue == null || sleepValue <= 0 -> {
                                viewModel.error.value = "Please enter valid sleep hours"
                            }
                            waterValue == null || waterValue < 0 -> {
                                viewModel.error.value = "Please enter valid water amount"
                            }
                            workValue == null || workValue < 0 -> {
                                viewModel.error.value = "Please enter valid work hours"
                            }
                            else -> {
                                viewModel.submitHabitsEntry(
                                    sleepHours = sleepValue,
                                    waterLitres = waterValue,
                                    workHours = workValue,
                                    onSuccess = {
                                        // 清空表单
                                        sleepHours = ""
                                        waterLitres = ""
                                        workHours = ""
                                        onNavigateToLifestyleLogged()
                                    },
                                    onError = { error ->
                                        viewModel.error.value = error
                                    }
                                )
                            }
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

