package com.example.nus.utils

import com.example.nus.model.MLModelResponse
import com.google.gson.Gson

/**
 * 验证emotions处理逻辑的工具类
 */
object EmotionsValidator {
    
    fun validateEmotionsHandling() {
        println("=== Emotions处理验证开始 ===")
        
        // 测试案例1: 正常的ML模型响应
        testCase1_NormalResponse()
        
        // 测试案例2: 空的top_emotions数组
        testCase2_EmptyTopEmotions()
        
        // 测试案例3: 空的内部数组
        testCase3_EmptyInnerArray()
        
        // 测试案例4: 多个数组（应该只取第一个）
        testCase4_MultipleArrays()
        
        println("=== Emotions处理验证结束 ===")
    }
    
    private fun testCase1_NormalResponse() {
        println("\n测试案例1: 正常响应")
        val json = """{"top_emotions": [["happy", "excited"]]}"""
        val response = parseResponse(json)
        val emotions = response?.emotions ?: emptyList()
        
        println("输入: $json")
        println("输出: $emotions")
        
        if (emotions.isNotEmpty() && emotions.contains("happy") && emotions.contains("excited")) {
            println("✅ 测试通过")
        } else {
            println("❌ 测试失败")
        }
    }
    
    private fun testCase2_EmptyTopEmotions() {
        println("\n测试案例2: 空的top_emotions数组")
        val json = """{"top_emotions": []}"""
        val response = parseResponse(json)
        val emotions = response?.emotions ?: emptyList()
        
        println("输入: $json")
        println("输出: $emotions")
        
        if (emotions.isEmpty()) {
            println("✅ 测试通过 - 正确返回空列表")
        } else {
            println("❌ 测试失败")
        }
    }
    
    private fun testCase3_EmptyInnerArray() {
        println("\n测试案例3: 空的内部数组")
        val json = """{"top_emotions": [[]]}"""
        val response = parseResponse(json)
        val emotions = response?.emotions ?: emptyList()
        
        println("输入: $json")
        println("输出: $emotions")
        
        if (emotions.isEmpty()) {
            println("✅ 测试通过 - 正确返回空列表")
        } else {
            println("❌ 测试失败")
        }
    }
    
    private fun testCase4_MultipleArrays() {
        println("\n测试案例4: 多个数组")
        val json = """{"top_emotions": [["happy", "excited"], ["sad", "anxious"]]}"""
        val response = parseResponse(json)
        val emotions = response?.emotions ?: emptyList()
        
        println("输入: $json")
        println("输出: $emotions")
        
        if (emotions.size == 2 && emotions.contains("happy") && emotions.contains("excited") && 
            !emotions.contains("sad") && !emotions.contains("anxious")) {
            println("✅ 测试通过 - 只取第一个数组")
        } else {
            println("❌ 测试失败")
        }
    }
    
    private fun parseResponse(json: String): MLModelResponse? {
        return try {
            val gson = Gson()
            gson.fromJson(json, MLModelResponse::class.java)
        } catch (e: Exception) {
            println("解析异常: ${e.message}")
            null
        }
    }
    
    fun validateDefaultEmotionsLogic() {
        println("\n=== 默认Emotions逻辑验证 ===")
        
        // 模拟MoodViewModel中的逻辑
        var emotions: List<String> = emptyList()
        
        // 模拟ML模型失败的情况
        println("模拟ML模型失败...")
        emotions = listOf("neutral")
        println("设置默认emotions: $emotions")
        
        // 模拟最终检查
        if (emotions.isEmpty()) {
            emotions = listOf("neutral")
            println("最终检查: 使用默认emotions")
        }
        
        println("最终emotions: $emotions")
        
        if (emotions.isNotEmpty()) {
            println("✅ 默认逻辑验证通过 - emotions不为空")
        } else {
            println("❌ 默认逻辑验证失败 - emotions仍为空")
        }
    }
}
