package com.example.nus.utils

import com.example.nus.api.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 调试CounsellorClientApi响应的工具类
 */
object CounsellorClientApiDebugger {
    
    fun testListClientsAPI() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("=== CounsellorClientApi调试开始 ===")
                println("API地址: http://122.248.243.60:8080/api/counsellor/clients/all")
                
                val response = ApiClient.counsellorClientApiService.listClients()
                
                println("响应状态码: ${response.code()}")
                println("响应消息: ${response.message()}")
                
                if (response.isSuccessful) {
                    val clients = response.body()
                    println("响应体: $clients")
                    
                    if (clients != null) {
                        println("客户数量: ${clients.size}")
                        
                        clients.forEachIndexed { index, client ->
                            println("客户 $index:")
                            println("  ID: ${client.id}")
                            println("  Email: ${client.email}")
                            println("  姓名: ${client.firstName} ${client.lastName}")
                            println("  显示情感: ${client.showEmotion}")
                            println("  已归档: ${client.archived}")
                        }
                        
                        if (clients.isEmpty()) {
                            println("⚠️ 警告: 客户列表为空！")
                        } else {
                            println("✅ 成功获取 ${clients.size} 个客户")
                        }
                    } else {
                        println("❌ 响应体为null")
                    }
                } else {
                    println("❌ CounsellorClientApi调用失败")
                    val errorBody = response.errorBody()?.string()
                    println("错误响应体: $errorBody")
                    
                    // 常见错误分析
                    when (response.code()) {
                        401 -> println("💡 提示: 可能是认证问题，请检查是否已登录")
                        403 -> println("💡 提示: 可能是权限问题，请检查用户是否为咨询师")
                        404 -> println("💡 提示: 端点不存在，请检查URL路径")
                        500 -> println("💡 提示: 服务器内部错误，请检查后端日志")
                    }
                }
                
                println("=== CounsellorClientApi调试结束 ===")
                
            } catch (e: Exception) {
                println("❌ CounsellorClientApi调用异常: ${e.message}")
                e.printStackTrace()
            }
        }
    }
    
    fun testClientJournalEntriesAPI(clientId: String = "test-client-id") {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("=== 客户日志条目API调试开始 ===")
                println("客户ID: $clientId")
                println("API地址: http://122.248.243.60:8080/api/counsellor/clients/$clientId/journal-entries")
                
                val response = ApiClient.counsellorClientApiService.listClientJournalEntries(clientId)
                
                println("响应状态码: ${response.code()}")
                println("响应消息: ${response.message()}")
                
                if (response.isSuccessful) {
                    val entries = response.body()
                    println("响应体: $entries")
                    
                    if (entries != null) {
                        println("日志条目数量: ${entries.size}")
                        
                        entries.forEachIndexed { index, entry ->
                            println("日志条目 $index:")
                            println("  ID: ${entry.id}")
                            println("  标题: ${entry.entryTitle}")
                            println("  内容: ${entry.entryText.take(50)}...")
                            println("  情绪: ${entry.emotions}")
                            println("  心情: ${entry.mood}")
                            println("  创建时间: ${entry.createdAt}")
                        }
                        
                        if (entries.isEmpty()) {
                            println("⚠️ 该客户暂无日志条目")
                        } else {
                            println("✅ 成功获取 ${entries.size} 个日志条目")
                        }
                    } else {
                        println("❌ 响应体为null")
                    }
                } else {
                    println("❌ 客户日志条目API调用失败")
                    val errorBody = response.errorBody()?.string()
                    println("错误响应体: $errorBody")
                }
                
                println("=== 客户日志条目API调试结束 ===")
                
            } catch (e: Exception) {
                println("❌ 客户日志条目API调用异常: ${e.message}")
                e.printStackTrace()
            }
        }
    }
    
    fun testAllAPIs() {
        println("🚀 开始测试所有CounsellorClientApi端点...")
        testListClientsAPI()
        
        // 延迟一秒后测试日志条目API
        CoroutineScope(Dispatchers.IO).launch {
            kotlinx.coroutines.delay(1000)
            testClientJournalEntriesAPI()
        }
    }
}
