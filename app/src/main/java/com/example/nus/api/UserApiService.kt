package com.example.nus.api

import com.example.nus.model.LoginRequest
import com.example.nus.model.LoginResponse
import com.example.nus.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApiService {
    
    @POST("api/user/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/user/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<String>

    @GET("api/user/logout")
    suspend fun logout(): Response<String>
}
