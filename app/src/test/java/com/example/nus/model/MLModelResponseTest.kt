package com.example.nus.model

import org.junit.Test
import org.junit.Assert.*
import com.google.gson.Gson

class MLModelResponseTest {

    @Test
    fun testMLModelResponseParsing() {
        // 模拟ML模型API的实际响应格式
        val jsonResponse = """{"top_emotions": [["anxious", "sad"]]}"""
        
        val gson = Gson()
        val response = gson.fromJson(jsonResponse, MLModelResponse::class.java)
        
        // 验证原始数据解析正确
        assertNotNull(response.topEmotions)
        assertEquals(1, response.topEmotions.size)
        assertEquals(2, response.topEmotions[0].size)
        assertEquals("anxious", response.topEmotions[0][0])
        assertEquals("sad", response.topEmotions[0][1])
        
        // 验证便捷属性返回正确的情感列表
        val emotions = response.emotions
        assertEquals(2, emotions.size)
        assertEquals("anxious", emotions[0])
        assertEquals("sad", emotions[1])
    }
    
    @Test
    fun testMLModelResponseWithEmptyArray() {
        // 测试空数组的情况
        val jsonResponse = """{"top_emotions": []}"""
        
        val gson = Gson()
        val response = gson.fromJson(jsonResponse, MLModelResponse::class.java)
        
        // 验证空数组情况下emotions返回空列表
        assertTrue(response.emotions.isEmpty())
    }
    
    @Test
    fun testMLModelResponseWithMultipleArrays() {
        // 测试多个数组的情况（只应该取第一个）
        val jsonResponse = """{"top_emotions": [["happy", "excited"], ["sad", "anxious"]]}"""
        
        val gson = Gson()
        val response = gson.fromJson(jsonResponse, MLModelResponse::class.java)
        
        // 验证只取第一个数组
        val emotions = response.emotions
        assertEquals(2, emotions.size)
        assertEquals("happy", emotions[0])
        assertEquals("excited", emotions[1])
    }
}
