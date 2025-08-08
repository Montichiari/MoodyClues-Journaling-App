package com.example.nus.model

import com.google.gson.annotations.SerializedName

data class HabitsEntryUpdateRequest(
    @SerializedName("sleep")
    val sleep: Double,
    @SerializedName("water")
    val water: Double,
    @SerializedName("workHours")
    val workHours: Double
)
