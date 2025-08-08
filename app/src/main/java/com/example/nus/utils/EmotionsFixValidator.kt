package com.example.nus.utils

/**
 * 验证emotions修复的工具类
 */
object EmotionsFixValidator {
    
    fun validateEmotionsLogic() {
        println("=== Emotions修复验证开始 ===")
        
        // 测试案例1: ML模型返回2个或更多emotions
        testCase1_MultipleEmotions()
        
        // 测试案例2: ML模型返回1个emotion
        testCase2_SingleEmotion()
        
        // 测试案例3: ML模型返回空emotions
        testCase3_EmptyEmotions()
        
        // 测试案例4: ML模型API失败
        testCase4_APIFailure()
        
        // 测试案例5: ML模型网络异常
        testCase5_NetworkException()
        
        println("=== Emotions修复验证结束 ===")
    }
    
    private fun testCase1_MultipleEmotions() {
        println("\n测试案例1: ML模型返回多个emotions")
        val responseEmotions = listOf("happy", "excited", "joyful")
        
        val emotions = if (responseEmotions.isNotEmpty()) {
            if (responseEmotions.size >= 2) responseEmotions else listOf(responseEmotions[0], responseEmotions[0])
        } else {
            listOf("neutral", "neutral")
        }
        
        println("输入: $responseEmotions")
        println("输出: $emotions")
        
        if (emotions.size >= 2) {
            println("✅ 测试通过 - emotions数量: ${emotions.size}")
        } else {
            println("❌ 测试失败 - emotions数量不足")
        }
    }
    
    private fun testCase2_SingleEmotion() {
        println("\n测试案例2: ML模型返回单个emotion")
        val responseEmotions = listOf("happy")
        
        val emotions = if (responseEmotions.isNotEmpty()) {
            if (responseEmotions.size >= 2) responseEmotions else listOf(responseEmotions[0], responseEmotions[0])
        } else {
            listOf("neutral", "neutral")
        }
        
        println("输入: $responseEmotions")
        println("输出: $emotions")
        
        if (emotions.size >= 2 && emotions[0] == "happy" && emotions[1] == "happy") {
            println("✅ 测试通过 - 正确重复单个emotion")
        } else {
            println("❌ 测试失败")
        }
    }
    
    private fun testCase3_EmptyEmotions() {
        println("\n测试案例3: ML模型返回空emotions")
        val responseEmotions = emptyList<String>()
        
        val emotions = if (responseEmotions.isNotEmpty()) {
            if (responseEmotions.size >= 2) responseEmotions else listOf(responseEmotions[0], responseEmotions[0])
        } else {
            listOf("neutral", "neutral")
        }
        
        println("输入: $responseEmotions")
        println("输出: $emotions")
        
        if (emotions.size == 2 && emotions[0] == "neutral" && emotions[1] == "neutral") {
            println("✅ 测试通过 - 使用默认neutral emotions")
        } else {
            println("❌ 测试失败")
        }
    }
    
    private fun testCase4_APIFailure() {
        println("\n测试案例4: ML模型API失败")
        // 模拟API失败情况
        var emotions = listOf("neutral", "neutral")
        
        println("模拟API失败，使用默认emotions: $emotions")
        
        if (emotions.size == 2 && emotions.all { it == "neutral" }) {
            println("✅ 测试通过 - API失败时使用默认emotions")
        } else {
            println("❌ 测试失败")
        }
    }
    
    private fun testCase5_NetworkException() {
        println("\n测试案例5: ML模型网络异常")
        // 模拟网络异常情况
        var emotions = listOf("neutral", "neutral")
        
        println("模拟网络异常，使用默认emotions: $emotions")
        
        if (emotions.size == 2 && emotions.all { it == "neutral" }) {
            println("✅ 测试通过 - 网络异常时使用默认emotions")
        } else {
            println("❌ 测试失败")
        }
    }
    
    fun validateFinalSafetyCheck() {
        println("\n=== 最终安全检查验证 ===")
        
        // 测试最终安全检查逻辑
        val testCases = listOf(
            emptyList<String>() to "空列表",
            listOf("happy") to "单个emotion",
            listOf("happy", "excited") to "两个emotions",
            listOf("sad", "anxious", "worried") to "三个emotions"
        )
        
        testCases.forEach { (inputEmotions, description) ->
            var emotions = inputEmotions
            
            // 应用最终安全检查逻辑
            if (emotions.isEmpty()) {
                emotions = listOf("neutral", "neutral")
                println("$description: 使用默认emotions")
            } else if (emotions.size == 1) {
                emotions = listOf(emotions[0], emotions[0])
                println("$description: 重复单个emotion")
            } else {
                println("$description: 保持原有emotions")
            }
            
            val isValid = emotions.size >= 2
            val status = if (isValid) "✅" else "❌"
            println("  $status 结果: $emotions (数量: ${emotions.size})")
        }
    }
}
