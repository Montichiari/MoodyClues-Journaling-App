package com.example.nus.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nus.api.ApiClient
import com.example.nus.model.LoginRequest
import com.example.nus.model.LoginResponse
import kotlinx.coroutines.launch

enum class UserType {
    USER, COUNSELLOR
}

class LoginViewModel : ViewModel() {

    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val loginError = mutableStateOf<String?>(null)
    val loginSuccess = mutableStateOf(false)
    val loginResponse = mutableStateOf<LoginResponse?>(null)
    val userType = mutableStateOf(UserType.USER)
    
    fun login(onSuccess: (LoginResponse) -> Unit, onError: (String) -> Unit) {
        if (email.value.isBlank() || password.value.isBlank()) {
            onError("Please enter both email and password")
            return
        }
        
        isLoading.value = true
        loginError.value = null
        
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(
                    email = email.value.trim(),
                    password = password.value
                )

                val userTypeText = if (userType.value == UserType.USER) "user" else "counsellor"
                println("Attempting $userTypeText login with email: ${loginRequest.email}")

                val response = if (userType.value == UserType.USER) {
                    ApiClient.userApiService.login(loginRequest)
                } else {
                    ApiClient.counsellorApiService.login(loginRequest)
                }

                println("Response received: Code=${response.code()}, Success=${response.isSuccessful}")
                
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        this@LoginViewModel.loginResponse.value = loginResponse
                        loginSuccess.value = true

                        // 设置API客户端的认证信息
                        ApiClient.setUserCredentials(email.value.trim(), password.value)

                        onSuccess(loginResponse)
                    } ?: run {
                        onError("Login failed: Empty response")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = when (response.code()) {
                        401 -> errorBody ?: "Invalid email or password"
                        404 -> "${userTypeText.replaceFirstChar { it.uppercase() }} not found"
                        500 -> "Server error. Please try again later"
                        else -> "Login failed: ${response.code()} - ${response.message()}"
                    }
                    println("Login error: Code=${response.code()}, Message=${response.message()}, Body=$errorBody")
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                onError("Network error: ${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }
    
    fun toggleUserType() {
        userType.value = if (userType.value == UserType.USER) UserType.COUNSELLOR else UserType.USER
        // 清除之前的错误信息
        loginError.value = null
    }

    fun clearError() {
        loginError.value = null
    }

    fun resetLoginState() {
        email.value = ""
        password.value = ""
        loginError.value = null
        loginSuccess.value = false
        loginResponse.value = null
        isLoading.value = false
        userType.value = UserType.USER
    }
}
