package com.example.nus.model

import java.time.LocalDate

data class LifestyleEntry(
    val id: String = System.currentTimeMillis().toString(),
    val date: LocalDate = LocalDate.now(),
    val sleepHours: Float = 0f,
    val waterGlasses: Int = 0,
    val didOvertime: Boolean = false,
    val exerciseMinutes: Int = 0,
    val stressLevel: Int = 0, // 0-10 scale
    val socialInteractionHours: Float = 0f,
    val notes: String = "",
    // 新增字段以匹配后端
    val userId: String = "",
    // 后端使用的字段（以升为单位的水量和工作小时）
    val waterLitres: Double = 0.0,
    val workHours: Double = 0.0
) {
    // 转换为后端请求格式
    fun toHabitsEntryRequest(): HabitsEntryRequest {
        return HabitsEntryRequest(
            userId = userId,
            sleep = sleepHours.toDouble(),
            water = waterLitres,
            workHours = workHours
        )
    }

    // 转换为后端更新请求格式
    fun toHabitsEntryUpdateRequest(): HabitsEntryUpdateRequest {
        return HabitsEntryUpdateRequest(
            sleep = sleepHours.toDouble(),
            water = waterLitres,
            workHours = workHours
        )
    }

    companion object {
        // 从后端响应创建LifestyleEntry
        fun fromHabitsEntryResponse(response: HabitsEntryResponse): LifestyleEntry {
            return LifestyleEntry(
                id = response.id,
                sleepHours = response.sleep.toFloat(),
                waterLitres = response.water,
                workHours = response.workHours,
                userId = response.userId,
                // 保留原有字段的默认值
                waterGlasses = (response.water * 4).toInt(), // 假设1升=4杯
                didOvertime = response.workHours > 8.0,
                exerciseMinutes = 0,
                stressLevel = 5,
                socialInteractionHours = 0f,
                notes = ""
            )
        }
    }
}