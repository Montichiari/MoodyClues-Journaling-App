package com.example.nus.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nus.api.ApiClient
import com.example.nus.model.HabitsEntryRequest
import com.example.nus.model.LifestyleEntry
import kotlinx.coroutines.launch
import java.time.LocalDate

class LifestyleViewModel : ViewModel() {
    private val _lifestyleEntries = mutableStateListOf<LifestyleEntry>()
    val lifestyleEntries: List<LifestyleEntry> get() = _lifestyleEntries

    val selectedDate = mutableStateOf(LocalDate.now())

    // Form state - 保持原有字段
    val sleepHours = mutableStateOf(7f)
    val waterGlasses = mutableStateOf(0)
    val didOvertime = mutableStateOf(false)
    val exerciseMinutes = mutableStateOf(0)
    val stressLevel = mutableStateOf(5)
    val socialInteractionHours = mutableStateOf(0f)
    val notes = mutableStateOf("")

    // 新增后端相关字段
    val waterLitres = mutableStateOf(0.0)
    val workHours = mutableStateOf(0.0)

    // 网络状态
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)
    val submitSuccess = mutableStateOf(false)

    // 当前用户ID
    var currentUserId: String = ""
    
    // 提交习惯条目到后端
    fun submitHabitsEntry(
        sleepHours: Double,
        waterLitres: Double,
        workHours: Double,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (currentUserId.isEmpty()) {
            onError("User not logged in")
            return
        }

        if (sleepHours <= 0) {
            onError("Please enter valid sleep hours")
            return
        }

        if (waterLitres < 0) {
            onError("Please enter valid water amount")
            return
        }

        if (workHours < 0) {
            onError("Please enter valid work hours")
            return
        }

        isLoading.value = true
        error.value = null
        submitSuccess.value = false

        viewModelScope.launch {
            try {
                val habitsRequest = HabitsEntryRequest(
                    userId = currentUserId,
                    sleep = sleepHours,
                    water = waterLitres,
                    workHours = workHours
                )

                println("Submitting habits entry: userId=$currentUserId, sleep=$sleepHours, water=$waterLitres, work=$workHours")
                val response = ApiClient.habitsApiService.submitHabitsEntry(habitsRequest)

                if (response.isSuccessful) {
                    // 读取响应体内容
                    val responseText = response.body()?.string() ?: "Success"
                    println("Habits backend response: $responseText")

                    // 添加到本地列表
                    saveLifestyleEntryLocal(sleepHours, waterLitres, workHours)
                    submitSuccess.value = true
                    onSuccess()
                    println("Habits entry submitted successfully")
                } else {
                    val errorMessage = "Failed to submit habits entry: ${response.code()} - ${response.message()}"
                    println("Submit error: $errorMessage")
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = "Network error: ${e.message}"
                println("Submit exception: $errorMessage")
                onError(errorMessage)
            } finally {
                isLoading.value = false
            }
        }
    }

    // 本地保存（保持原有功能）
    private fun saveLifestyleEntryLocal(sleepHours: Double, waterLitres: Double, workHours: Double) {
        val existingEntryIndex = _lifestyleEntries.indexOfFirst {
            it.date == selectedDate.value
        }

        val entry = LifestyleEntry(
            date = selectedDate.value,
            sleepHours = sleepHours.toFloat(),
            waterLitres = waterLitres,
            workHours = workHours,
            waterGlasses = (waterLitres * 4).toInt(), // 1升=4杯
            didOvertime = workHours > 8.0,
            exerciseMinutes = exerciseMinutes.value,
            stressLevel = stressLevel.value,
            socialInteractionHours = socialInteractionHours.value,
            notes = notes.value,
            userId = currentUserId
        )

        if (existingEntryIndex >= 0) {
            _lifestyleEntries[existingEntryIndex] = entry.copy(id = _lifestyleEntries[existingEntryIndex].id)
        } else {
            _lifestyleEntries.add(entry)
        }
    }

    // 兼容性方法（保持原有API）
    fun saveLifestyleEntry() {
        saveLifestyleEntryLocal(
            sleepHours.value.toDouble(),
            waterLitres.value,
            workHours.value
        )
        resetForm()
    }
    
    // 获取所有习惯条目
    fun loadAllHabitsEntries(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (currentUserId.isEmpty()) {
            onError("User not logged in")
            return
        }

        isLoading.value = true
        error.value = null

        viewModelScope.launch {
            try {
                val response = ApiClient.habitsApiService.getAllHabitsEntries(currentUserId)

                if (response.isSuccessful) {
                    response.body()?.let { habitsEntries ->
                        _lifestyleEntries.clear()
                        _lifestyleEntries.addAll(habitsEntries.map { LifestyleEntry.fromHabitsEntryResponse(it) })
                        onSuccess()
                        println("Loaded ${habitsEntries.size} habits entries")
                    } ?: run {
                        onError("Empty response from server")
                    }
                } else {
                    val errorMessage = "Failed to load habits entries: ${response.code()} - ${response.message()}"
                    println("Load error: $errorMessage")
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = "Network error: ${e.message}"
                println("Load exception: $errorMessage")
                onError(errorMessage)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun loadEntryForDate(date: LocalDate) {
        selectedDate.value = date

        val entry = _lifestyleEntries.find { it.date == date }
        if (entry != null) {
            sleepHours.value = entry.sleepHours
            waterGlasses.value = entry.waterGlasses
            waterLitres.value = entry.waterLitres
            workHours.value = entry.workHours
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
        waterLitres.value = 0.0
        workHours.value = 0.0
        didOvertime.value = false
        exerciseMinutes.value = 0
        stressLevel.value = 5
        socialInteractionHours.value = 0f
        notes.value = ""
    }

    fun clearError() {
        error.value = null
    }

    fun setUserId(userId: String) {
        println("LifestyleViewModel: Setting userId from '$currentUserId' to '$userId'")
        currentUserId = userId
        println("LifestyleViewModel: currentUserId is now '$currentUserId'")
    }
} 