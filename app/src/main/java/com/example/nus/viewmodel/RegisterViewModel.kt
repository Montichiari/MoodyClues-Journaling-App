package com.example.nus.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nus.api.ApiClient
import com.example.nus.data.PersistentUserManager
import com.example.nus.model.RegisterRequest
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val userManager = PersistentUserManager.getInstance(application)

    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val firstName = mutableStateOf("")
    val lastName = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val registerError = mutableStateOf<String?>(null)
    val registerSuccess = mutableStateOf(false)
    
    // 生成测试邮箱的辅助函数
    fun generateTestEmail(): String {
        val timestamp = System.currentTimeMillis()
        return "test$timestamp@example.com"
    }

    fun register(onSuccess: () -> Unit, onError: (String) -> Unit) {
        // 验证输入
        if (email.value.isBlank()) {
            onError("Please enter your email")
            return
        }
        
        if (password.value.isBlank()) {
            onError("Please enter your password")
            return
        }
        
        if (firstName.value.isBlank()) {
            onError("Please enter your first name")
            return
        }
        
        if (lastName.value.isBlank()) {
            onError("Please enter your last name")
            return
        }
        
        // 简单的邮箱格式验证
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            onError("Please enter a valid email address")
            return
        }
        
        // 密码复杂度验证（与后端保持一致）
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$"
        if (!password.value.matches(passwordPattern.toRegex())) {
            onError("Password must be at least 8 characters long, include uppercase and lowercase letters, a number, and a special character (@\$!%*?&)")
            return
        }
        
        isLoading.value = true
        registerError.value = null
        
        viewModelScope.launch {
            try {
                val registerRequest = RegisterRequest(
                    email = email.value.trim(),
                    password = password.value,
                    firstName = firstName.value.trim(),
                    lastName = lastName.value.trim()
                )

                println("Attempting registration with email: ${registerRequest.email}")
                println("Password being sent: ${registerRequest.password}")

                val response = ApiClient.userApiService.register(registerRequest)
                println("Registration response: Code=${response.code()}, Success=${response.isSuccessful}")

                if (response.isSuccessful) {
                    val responseBody = response.body()?.string() ?: "Registration successful"
                    println("Registration successful! Response: $responseBody")

                    // 注册成功后，将用户添加到本地数据库
                    userManager.addUser(
                        firstName = firstName.value.trim(),
                        lastName = lastName.value.trim(),
                        email = email.value.trim()
                    )

                    registerSuccess.value = true
                    onSuccess()
                } else {
                    // 详细处理错误响应
                    val errorBody = response.errorBody()?.string()
                    println("Error response body: $errorBody")

                    val errorMessage = when (response.code()) {
                        400 -> {
                            // 400错误通常表示邮箱已存在或数据验证失败
                            "Email already exists or invalid data. Please try a different email or check your password format (8+ chars, uppercase, lowercase, number, special char @\$!%*?&)"
                        }
                        409 -> "Email already exists. Please use a different email"
                        500 -> "Server error. Please try again later"
                        else -> "Registration failed: ${response.code()} - ${response.message()}"
                    }
                    println("Registration error: Code=${response.code()}, Message=${response.message()}, Body=$errorBody")
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                println("Registration exception: ${e.message}")
                println("Exception type: ${e.javaClass.simpleName}")
                e.printStackTrace()

                // 更好的错误处理
                val errorMessage = when {
                    e.message?.contains("timeout") == true -> "Connection timeout. Please check your internet connection"
                    e.message?.contains("Unable to resolve host") == true -> "Cannot connect to server. Please check your internet connection"
                    e.message?.contains("not found") == true -> "Server response format error. Registration may have succeeded - please try logging in"
                    else -> "Network error: ${e.message}"
                }
                onError(errorMessage)
            } finally {
                isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        registerError.value = null
    }
    
    fun resetRegisterState() {
        email.value = ""
        password.value = ""
        firstName.value = ""
        lastName.value = ""
        registerError.value = null
        registerSuccess.value = false
        isLoading.value = false
    }
}
