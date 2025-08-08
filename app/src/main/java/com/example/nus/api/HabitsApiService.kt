package com.example.nus.api

import com.example.nus.model.HabitsEntryRequest
import com.example.nus.model.HabitsEntryResponse
import com.example.nus.model.HabitsEntryUpdateRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface HabitsApiService {
    
    @POST("api/habits/submit")
    suspend fun submitHabitsEntry(@Body habitsEntryRequest: HabitsEntryRequest): Response<ResponseBody>
    
    @GET("api/habits/all")
    suspend fun getAllHabitsEntries(@Query("userId") userId: String): Response<List<HabitsEntryResponse>>
    
    @GET("api/habits/{entryId}")
    suspend fun getHabitsEntryById(@Path("entryId") entryId: String): Response<HabitsEntryResponse>
    
    @PUT("api/habits/{entryId}/edit")
    suspend fun updateHabitsEntry(
        @Path("entryId") entryId: String,
        @Body updateRequest: HabitsEntryUpdateRequest
    ): Response<String>
    
    @PUT("api/habits/{entryId}/archive")
    suspend fun archiveHabitsEntry(@Path("entryId") entryId: String): Response<String>
}
