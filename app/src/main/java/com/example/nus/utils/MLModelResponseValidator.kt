package com.example.nus.utils

import com.example.nus.model.MLModelResponse
import com.google.gson.Gson

/**
 * 用于验证MLModelResponse修复的工具类
 */
object MLModelResponseValidator {
    
    /**
     * 验证ML模型响应解析是否正确
     */
    fun validateMLModelResponse(): Boolean {
        return try {
            // 模拟ML模型API的实际响应
            val actualMLResponse = """{"top_emotions": [["anxious", "sad"]]}"""
            
            val gson = Gson()
            val response = gson.fromJson(actualMLResponse, MLModelResponse::class.java)
            
            // 检查解析结果
            val emotions = response.emotions
            
            println("ML模型响应验证:")
            println("原始响应: $actualMLResponse")
            println("解析后的topEmotions: ${response.topEmotions}")
            println("便捷属性emotions: $emotions")
            
            // 验证是否符合预期
            val isValid = emotions.isNotEmpty() && 
                         emotions.contains("anxious") && 
                         emotions.contains("sad")
            
            if (isValid) {
                println("✅ ML模型响应解析成功！")
            } else {
                println("❌ ML模型响应解析失败！")
            }
            
            isValid
        } catch (e: Exception) {
            println("❌ ML模型响应解析异常: ${e.message}")
            false
        }
    }
    
    /**
     * 测试不同的响应格式
     */
    fun testDifferentResponseFormats() {
        val testCases = listOf(
            """{"top_emotions": [["happy"]]}""" to listOf("happy"),
            """{"top_emotions": [["sad", "anxious"]]}""" to listOf("sad", "anxious"),
            """{"top_emotions": []}""" to emptyList(),
            """{"top_emotions": [["neutral"], ["happy"]]}""" to listOf("neutral") // 只取第一个
        )
        
        val gson = Gson()
        
        testCases.forEachIndexed { index, (json, expected) ->
            try {
                val response = gson.fromJson(json, MLModelResponse::class.java)
                val actual = response.emotions
                
                val isCorrect = actual == expected
                val status = if (isCorrect) "✅" else "❌"
                
                println("测试案例 ${index + 1}: $status")
                println("  输入: $json")
                println("  期望: $expected")
                println("  实际: $actual")
                println()
            } catch (e: Exception) {
                println("❌ 测试案例 ${index + 1} 异常: ${e.message}")
            }
        }
    }
}
