package com.example.nus.model

import com.google.gson.annotations.SerializedName

data class HabitsEntryRequest(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("sleep")
    val sleep: Double,
    @SerializedName("water")
    val water: Double,
    @SerializedName("workHours")
    val workHours: Double
)
