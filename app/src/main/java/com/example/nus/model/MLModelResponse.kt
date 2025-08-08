package com.example.nus.model

import com.google.gson.annotations.SerializedName

data class MLModelResponse(
    @SerializedName("top_emotions")
    val topEmotions: List<List<String>>
) {
    // 提供一个便捷的属性来获取第一个情感数组
    // ML模型返回格式: {"top_emotions": [["anxious", "sad"]]}
    // 我们只需要第一个数组中的情感列表
    val emotions: List<String>
        get() = topEmotions.firstOrNull() ?: emptyList()
}
