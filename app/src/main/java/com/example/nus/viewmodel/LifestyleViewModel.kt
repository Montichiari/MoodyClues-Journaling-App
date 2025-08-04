package com.example.nus.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.nus.model.LifestyleEntry
import java.time.LocalDate

class LifestyleViewModel : ViewModel() {
    private val _lifestyleEntries = mutableStateListOf<LifestyleEntry>()
    val lifestyleEntries: List<LifestyleEntry> get() = _lifestyleEntries
    
    val selectedDate = mutableStateOf(LocalDate.now())
    
    // Form state
    val sleepHours = mutableStateOf(7f)
    val waterGlasses = mutableStateOf(0)
    val didOvertime = mutableStateOf(false)
    val exerciseMinutes = mutableStateOf(0)
    val stressLevel = mutableStateOf(5)
    val socialInteractionHours = mutableStateOf(0f)
    val notes = mutableStateOf("")
    
    fun saveLifestyleEntry() {
        val existingEntryIndex = _lifestyleEntries.indexOfFirst { 
            it.date == selectedDate.value 
        }
        
        val entry = LifestyleEntry(
            date = selectedDate.value,
            sleepHours = sleepHours.value,
            waterGlasses = waterGlasses.value,
            didOvertime = didOvertime.value,
            exerciseMinutes = exerciseMinutes.value,
            stressLevel = stressLevel.value,
            socialInteractionHours = socialInteractionHours.value,
            notes = notes.value
        )
        
        if (existingEntryIndex >= 0) {
            _lifestyleEntries[existingEntryIndex] = entry.copy(id = _lifestyleEntries[existingEntryIndex].id)
        } else {
            _lifestyleEntries.add(entry)
        }
        
        resetForm()
    }
    
    fun loadEntryForDate(date: LocalDate) {
        selectedDate.value = date
        
        val entry = _lifestyleEntries.find { it.date == date }
        if (entry != null) {
            sleepHours.value = entry.sleepHours
            waterGlasses.value = entry.waterGlasses
            didOvertime.value = entry.didOvertime
            exerciseMinutes.value = entry.exerciseMinutes
            stressLevel.value = entry.stressLevel
            socialInteractionHours.value = entry.socialInteractionHours
            notes.value = entry.notes
        } else {
            resetForm()
        }
    }
    
    private fun resetForm() {
        sleepHours.value = 7f
        waterGlasses.value = 0
        didOvertime.value = false
        exerciseMinutes.value = 0
        stressLevel.value = 5
        socialInteractionHours.value = 0f
        notes.value = ""
    }
} 