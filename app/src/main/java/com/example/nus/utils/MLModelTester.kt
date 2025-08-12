package com.example.nus.utils

import com.example.nus.api.ApiClient
import com.example.nus.model.MLModelRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MLModelTester {
    
    fun testConnection(
        onSuccess: (List<String>) -> Unit,
        onError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("Testing ML model connection to: http://18.141.76.63:5000/predict")
                
                val testRequest = MLModelRequest(text = "I am feeling happy today")
                val response = ApiClient.mlModelApiService.predictEmotions(testRequest)
                
                if (response.isSuccessful) {
                    val emotions = response.body()?.emotions ?: emptyList()
                    println("ML model test successful. Emotions: $emotions")
                    onSuccess(emotions)
                } else {
                    val errorMsg = "ML model test failed: ${response.code()} - ${response.message()}"
                    println(errorMsg)
                    onError(errorMsg)
                }
            } catch (e: Exception) {
                val errorMsg = "ML model connection error: ${e.message}"
                println(errorMsg)
                e.printStackTrace()
                onError(errorMsg)
            }
        }
    }
}
