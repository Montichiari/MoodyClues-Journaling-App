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
        
        // 密码长度验证
        if (password.value.length < 6) {
            onError("Password must be at least 6 characters long")
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
                val response = ApiClient.userApiService.register(registerRequest)
                println("Registration response: Code=${response.code()}, Success=${response.isSuccessful}")
                
                if (response.isSuccessful) {
                    // 注册成功后，将用户添加到本地数据库
                    userManager.addUser(
                        firstName = firstName.value.trim(),
                        lastName = lastName.value.trim(),
                        email = email.value.trim()
                    )

                    registerSuccess.value = true
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = when (response.code()) {
                        400 -> errorBody ?: "Invalid registration data"
                        409 -> "Email already exists. Please use a different email"
                        500 -> "Server error. Please try again later"
                        else -> "Registration failed: ${response.code()} - ${response.message()}"
                    }
                    println("Registration error: Code=${response.code()}, Message=${response.message()}, Body=$errorBody")
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                println("Registration exception: ${e.message}")
                onError("Network error: ${e.message}")
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
