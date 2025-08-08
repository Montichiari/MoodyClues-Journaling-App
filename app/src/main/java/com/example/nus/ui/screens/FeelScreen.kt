package com.example.nus.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.nus.model.FeelType
import com.example.nus.viewmodel.FeelViewModel
import com.example.nus.viewmodel.MoodViewModel
import androidx.compose.runtime.getValue
import java.time.LocalDate

@Composable
fun FeelScreen(
    viewModel: FeelViewModel,
    moodViewModel: MoodViewModel, // 添加MoodViewModel参数
    onNavigateToHome: () -> Unit = {}
) {
    var selectedFeel by remember { mutableStateOf<FeelType?>(null) }

    // 获取ML模型预测的情感
    val predictedEmotions by moodViewModel.predictedEmotions
    
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
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                // Category icons row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CategoryIcon(
                        label = "Mood",
                        isSelected = false,
                        onClick = { }
                    )
                    CategoryIcon(
                        label = "Lifestyle",
                        isSelected = false,
                        onClick = { }
                    )
                    CategoryIcon(
                        label = "Journal",
                        isSelected = false,
                        onClick = { }
                    )
                    CategoryIcon(
                        label = "Emotions",
                        isSelected = true,
                        onClick = { }
                    )
                }
                
                // "Today, it seems like you felt..." text
                Text(
                    text = "Today, it seems like you felt...",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // 显示ML模型预测的情感
                if (predictedEmotions.isNotEmpty()) {
                    Text(
                        text = "AI detected emotions:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // 显示预测的情感标签
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                    ) {
                        predictedEmotions.take(5).forEach { emotion ->
                            Card(
                                modifier = Modifier.padding(2.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = emotion.replaceFirstChar { it.uppercase() },
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
                
                // Feel selection buttons
                val feelTypes = if (predictedEmotions.isNotEmpty()) {
                    mapPredictedEmotionsToFeelTypes(predictedEmotions)
                } else {
                    listOf(FeelType.HAPPY, FeelType.EXCITED) // 默认选项
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    feelTypes.forEach { feelType ->
                        FeelOption(
                            feelType = feelType,
                            label = feelType.name.lowercase().replaceFirstChar { it.uppercase() },
                            emoji = getEmojiForFeelType(feelType),
                            isSelected = selectedFeel == feelType,
                            onSelect = {
                                selectedFeel = feelType
                                viewModel.addFeelEntry(feelType)
                            }
                        )
                    }
                }
                
                // Quote section
                if (selectedFeel != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = viewModel.getCurrentQuote(),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Start,
                            lineHeight = 20.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Go Home button
                Button(
                    onClick = onNavigateToHome,// 增加了导航回主页面的方式
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Go Home",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryIcon(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primary 
                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Home, // You can customize icons for each category
                contentDescription = label,
                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Composable
fun FeelOption(
    feelType: FeelType,
    label: String,
    emoji: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onSelect() }
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    else Color.Transparent
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 32.sp
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

// 辅助函数：将ML模型预测的情感映射到FeelType
private fun mapPredictedEmotionsToFeelTypes(emotions: List<String>): List<FeelType> {
    val feelTypeMap = mapOf(
        "happy" to FeelType.HAPPY,
        "joy" to FeelType.HAPPY,
        "excited" to FeelType.EXCITED,
        "excitement" to FeelType.EXCITED,
        "sad" to FeelType.SAD,
        "sadness" to FeelType.SAD,
        "anxious" to FeelType.ANXIOUS,
        "anxiety" to FeelType.ANXIOUS,
        "worry" to FeelType.ANXIOUS,
        "neutral" to FeelType.NEUTRAL,
        "calm" to FeelType.NEUTRAL,
        "peaceful" to FeelType.NEUTRAL
    )

    return emotions.mapNotNull { emotion ->
        feelTypeMap[emotion.lowercase()]
    }.distinct().take(5).ifEmpty {
        listOf(FeelType.HAPPY, FeelType.EXCITED) // 默认显示选项
    }
}

// 辅助函数：为FeelType获取对应的emoji
private fun getEmojiForFeelType(feelType: FeelType): String {
    return when (feelType) {
        FeelType.HAPPY -> "😊"
        FeelType.EXCITED -> "🎉"
        FeelType.SAD -> "😢"
        FeelType.ANXIOUS -> "😰"
        FeelType.NEUTRAL -> "😐"
    }
}
