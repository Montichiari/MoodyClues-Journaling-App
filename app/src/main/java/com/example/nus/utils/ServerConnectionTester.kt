package com.example.nus.utils

import com.example.nus.api.ApiClient
import com.example.nus.model.LoginRequest
import com.example.nus.model.JournalEntryResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 测试服务器连接和登录功能的工具类
 */
object ServerConnectionTester {
    
    /**
     * 测试Counsellor登录功能
     */
    fun testCounsellorLogin(
        email: String = "dr.sarah.connor@clinic.com",
        password: String = "password"
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("=== Counsellor登录测试开始 ===")
                println("服务器地址: http://122.248.243.60:8080")
                println("登录端点: /api/counsellor/login")
                println("测试邮箱: $email")
                
                val loginRequest = LoginRequest(
                    email = email,
                    password = password
                )
                
                val response = ApiClient.counsellorApiService.login(loginRequest)
                
                println("响应状态码: ${response.code()}")
                println("响应消息: ${response.message()}")
                println("请求成功: ${response.isSuccessful}")
                
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    println("✅ 登录成功!")
                    println("用户ID: ${loginResponse?.userId}")
                    println("响应体: $loginResponse")
                } else {
                    println("❌ 登录失败")
                    val errorBody = response.errorBody()?.string()
                    println("错误响应体: $errorBody")
                    
                    when (response.code()) {
                        401 -> println("原因: 邮箱或密码错误")
                        404 -> println("原因: Counsellor账户不存在")
                        500 -> println("原因: 服务器内部错误")
                        else -> println("原因: 未知错误")
                    }
                }
                
                println("=== Counsellor登录测试结束 ===")
                
            } catch (e: Exception) {
                println("❌ 登录测试异常: ${e.message}")
                println("异常类型: ${e.javaClass.simpleName}")
                e.printStackTrace()
                
                when {
                    e.message?.contains("timeout") == true -> {
                        println("建议: 检查网络连接或服务器是否运行")
                    }
                    e.message?.contains("Unable to resolve host") == true -> {
                        println("建议: 检查服务器地址是否正确")
                    }
                    e.message?.contains("Connection refused") == true -> {
                        println("建议: 服务器可能未启动或端口被阻塞")
                    }
                }
            }
        }
    }
    
    /**
     * 测试User登录功能
     */
    fun testUserLogin(
        email: String = "alice.johnson@email.com",
        password: String = "password"
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("=== User登录测试开始 ===")
                println("服务器地址: http://122.248.243.60:8080")
                println("登录端点: /api/user/login")
                println("测试邮箱: $email")
                
                val loginRequest = LoginRequest(
                    email = email,
                    password = password
                )
                
                val response = ApiClient.userApiService.login(loginRequest)
                
                println("响应状态码: ${response.code()}")
                println("响应消息: ${response.message()}")
                println("请求成功: ${response.isSuccessful}")
                
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    println("✅ 登录成功!")
                    println("用户ID: ${loginResponse?.userId}")
                    println("响应体: $loginResponse")
                } else {
                    println("❌ 登录失败")
                    val errorBody = response.errorBody()?.string()
                    println("错误响应体: $errorBody")
                    
                    when (response.code()) {
                        401 -> println("原因: 邮箱或密码错误")
                        404 -> println("原因: User账户不存在")
                        500 -> println("原因: 服务器内部错误")
                        else -> println("原因: 未知错误")
                    }
                }
                
                println("=== User登录测试结束 ===")
                
            } catch (e: Exception) {
                println("❌ 登录测试异常: ${e.message}")
                println("异常类型: ${e.javaClass.simpleName}")
                e.printStackTrace()
            }
        }
    }
    
    /**
     * 测试服务器基本连接
     */
    fun testServerConnection() {
        println("=== 服务器连接测试 ===")
        println("正在测试连接到: http://122.248.243.60:8080")

        // 可以添加简单的ping测试或健康检查
        testCounsellorLogin()
    }

    /**
     * 测试JSON解析功能
     */
    fun testJSONParsing() {
        println("=== JSON解析测试开始 ===")

        // 模拟后端返回的Emotion对象格式
        val testJson = """
        {
            "id": "test-id",
            "userId": "alice-id",
            "mood": 4,
            "entryTitle": "Test Entry",
            "entryText": "Test content",
            "emotions": [
                {
                    "id": "emotion-1",
                    "emotionLabel": "happy",
                    "iconAddress": null
                },
                {
                    "id": "emotion-2",
                    "emotionLabel": "excited",
                    "iconAddress": ""
                }
            ],
            "createdAt": "2025-01-01T10:00:00",
            "archived": false
        }
        """.trimIndent()

        try {
            val gson = com.google.gson.Gson()
            val response = gson.fromJson(testJson, JournalEntryResponse::class.java)

            println("✅ JSON解析成功!")
            println("条目ID: ${response.id}")
            println("用户ID: ${response.userId}")
            println("情绪评分: ${response.mood}")
            println("标题: ${response.entryTitle}")
            println("情感数量: ${response.emotions.size}")

            response.emotions.forEachIndexed { index, emotion ->
                println("情感 $index: ${emotion.emotionLabel} (ID: ${emotion.id})")
            }

            // 测试转换为JournalEntry
            val journalEntry = response.toJournalEntry()
            println("转换后的JournalEntry情感数量: ${journalEntry.emotions.size}")
            journalEntry.emotions.forEachIndexed { index, emotion ->
                println("JournalEntry情感 $index: ${emotion.emotionLabel}")
            }

            println("=== JSON解析测试成功 ===")

        } catch (e: Exception) {
            println("❌ JSON解析失败: ${e.message}")
            e.printStackTrace()
        }
    }
}
