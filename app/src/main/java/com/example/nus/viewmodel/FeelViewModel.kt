package com.example.nus.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.nus.model.FeelEntry
import com.example.nus.model.FeelType
import java.time.LocalDate

class FeelViewModel : ViewModel() {
    private val _feelEntries = mutableStateListOf<FeelEntry>()
    val feelEntries: List<FeelEntry> get() = _feelEntries
    
    val selectedDate = mutableStateOf(LocalDate.now())
    val selectedFeel = mutableStateOf<FeelType?>(null)
    
    // Predefined quotes for different feel types
    private val quotes = mapOf(
        FeelType.HAPPY to listOf(
            "Nothing in the world is as contagious as laughter and good humor. — Charles Dickens",
            "Happiness is not something ready made. It comes from your own actions. — Dalai Lama",
            "The purpose of our lives is to be happy. — Dalai Lama"
        ),
        FeelType.EXCITED to listOf(
            "The way to get started is to quit talking and begin doing. — Walt Disney",
            "Life is what happens to you while you're busy making other plans. — John Lennon",
            "The future belongs to those who believe in the beauty of their dreams. — Eleanor Roosevelt"
        ),
        FeelType.NEUTRAL to listOf(
            "In the middle of difficulty lies opportunity. — Albert Einstein",
            "Life is 10% what happens to you and 90% how you react to it. — Charles R. Swindoll",
            "The only way to do great work is to love what you do. — Steve Jobs"
        ),
        FeelType.SAD to listOf(
            "The wound is the place where the Light enters you. — Rumi",
            "Every moment is a fresh beginning. — T.S. Eliot",
            "What lies behind us and what lies before us are tiny matters compared to what lies within us. — Ralph Waldo Emerson"
        ),
        FeelType.ANXIOUS to listOf(
            "You have been assigned this mountain to show others it can be moved. — Mel Robbins",
            "Anxiety is the dizziness of freedom. — Søren Kierkegaard",
            "The cave you fear to enter holds the treasure you seek. — Joseph Campbell"
        )
    )
    
    fun addFeelEntry(feelType: FeelType) {
        val quote = getRandomQuote(feelType)
        val entry = FeelEntry(
            feelType = feelType,
            date = selectedDate.value,
            quote = quote
        )
        
        // Remove existing entry for the same date if exists
        _feelEntries.removeAll { it.date == selectedDate.value }
        _feelEntries.add(entry)
        selectedFeel.value = feelType
    }
    
    fun getFeelForDate(date: LocalDate): FeelType? {
        return _feelEntries.find { it.date == date }?.feelType
    }
    
    fun getQuoteForDate(date: LocalDate): String {
        return _feelEntries.find { it.date == date }?.quote ?: ""
    }
    
    private fun getRandomQuote(feelType: FeelType): String {
        val quoteList = quotes[feelType] ?: quotes[FeelType.NEUTRAL]!!
        return quoteList.random()
    }
    
    fun getCurrentQuote(): String {
        return selectedFeel.value?.let { feelType ->
            getRandomQuote(feelType)
        } ?: "Nothing in the world is as contagious as laughter and good humor. — Charles Dickens"
    }
}
