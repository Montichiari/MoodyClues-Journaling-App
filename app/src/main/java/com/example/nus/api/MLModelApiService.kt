package com.example.nus.api

import com.example.nus.model.MLModelRequest
import com.example.nus.model.MLModelResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MLModelApiService {
    @POST("predict")
    suspend fun predictEmotions(@Body request: MLModelRequest): Response<MLModelResponse>
}
