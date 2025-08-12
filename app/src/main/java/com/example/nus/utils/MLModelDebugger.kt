package com.example.nus.utils

import com.example.nus.api.ApiClient
import com.example.nus.model.MLModelRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 调试ML模型API响应的工具类
 */
object MLModelDebugger {
    
    fun testMLModelAPI(text: String = "good I want to play outside, it makes me happy, but there are some wind make me excited") {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("=== ML模型API调试开始 ===")
                println("测试文本: $text")
                println("API地址: http://18.141.76.63:5000/predict")
                
                val request = MLModelRequest(text = text)
                val response = ApiClient.mlModelApiService.predictEmotions(request)
                
                println("响应状态码: ${response.code()}")
                println("响应消息: ${response.message()}")
                
                if (response.isSuccessful) {
                    val body = response.body()
                    println("响应体: $body")
                    
                    if (body != null) {
                        println("topEmotions: ${body.topEmotions}")
                        println("emotions属性: ${body.emotions}")
                        println("emotions大小: ${body.emotions.size}")
                        
                        if (body.emotions.isEmpty()) {
                            println("❌ 警告: emotions列表为空！这会导致418错误")
                        } else {
                            println("✅ emotions列表不为空: ${body.emotions}")
                        }
                    } else {
                        println("❌ 响应体为null")
                    }
                } else {
                    println("❌ ML模型API调用失败")
                    val errorBody = response.errorBody()?.string()
                    println("错误响应体: $errorBody")
                }
                
                println("=== ML模型API调试结束 ===")
                
            } catch (e: Exception) {
                println("❌ ML模型API调用异常: ${e.message}")
                e.printStackTrace()
            }
        }
    }
    
    fun testJSONParsing() {
        try {
            println("=== JSON解析测试开始 ===")
            
            // 测试实际的ML模型响应格式
            val testJson = """{"top_emotions": [["happy", "excited"]]}"""
            println("测试JSON: $testJson")
            
            val gson = com.google.gson.Gson()
            val response = gson.fromJson(testJson, com.example.nus.model.MLModelResponse::class.java)
            
            println("解析结果:")
            println("  topEmotions: ${response.topEmotions}")
            println("  emotions: ${response.emotions}")
            
            if (response.emotions.isNotEmpty()) {
                println("✅ JSON解析成功，emotions不为空")
            } else {
                println("❌ JSON解析失败，emotions为空")
            }
            
            println("=== JSON解析测试结束 ===")
            
        } catch (e: Exception) {
            println("❌ JSON解析异常: ${e.message}")
            e.printStackTrace()
        }
    }
}
