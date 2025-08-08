package com.example.nus.api

import com.example.nus.model.LoginRequest
import com.example.nus.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CounsellorApiService {
    
    @POST("api/counsellor/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("api/counsellor/logout")
    suspend fun logout(): Response<String>
}
