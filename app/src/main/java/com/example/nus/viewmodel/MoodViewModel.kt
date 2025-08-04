package com.example.nus.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.nus.model.MoodEntry
import com.example.nus.model.MoodType
import com.example.nus.model.TimeOfDay
import java.time.LocalDate

class MoodViewModel : ViewModel() {
    private val _moodEntries = mutableStateListOf<MoodEntry>()
    val moodEntries: List<MoodEntry> get() = _moodEntries
    
    val selectedDate = mutableStateOf(LocalDate.now())
    
    fun addMoodEntry(mood: MoodType, timeOfDay: TimeOfDay, notes: String = "") {
        // Check if an entry already exists for this time of day and date
        val existingEntryIndex = _moodEntries.indexOfFirst { 
            it.timeOfDay == timeOfDay && it.date == selectedDate.value 
        }
        
        // If entry exists, update it; otherwise add new entry
        if (existingEntryIndex >= 0) {
            _moodEntries[existingEntryIndex] = MoodEntry(
                id = _moodEntries[existingEntryIndex].id,
                mood = mood,
                timeOfDay = timeOfDay,
                date = selectedDate.value,
                notes = notes
            )
        } else {
            _moodEntries.add(
                MoodEntry(
                    mood = mood,
                    timeOfDay = timeOfDay,
                    date = selectedDate.value,
                    notes = notes
                )
            )
        }
    }
    
    fun getMoodForTimeOfDay(timeOfDay: TimeOfDay): MoodType? {
        return _moodEntries.find { 
            it.timeOfDay == timeOfDay && it.date == selectedDate.value 
        }?.mood
    }
    
    fun changeSelectedDate(date: LocalDate) {
        selectedDate.value = date
    }
} 