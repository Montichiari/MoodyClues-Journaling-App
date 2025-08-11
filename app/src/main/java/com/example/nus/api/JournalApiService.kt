package com.example.nus.api

import com.example.nus.model.JournalEntryRequest
import com.example.nus.model.JournalEntryResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface JournalApiService {
    
    @POST("api/journal/submit")
    suspend fun submitJournalEntry(@Body journalEntryRequest: JournalEntryRequest): Response<ResponseBody>
    
    @GET("api/journal/")
    suspend fun getAllJournalEntries(@Query("userId") userId: String): Response<List<JournalEntryResponse>>

    @GET("api/journal/{entryId}")
    suspend fun getJournalEntryById(@Path("entryId") entryId: String): Response<JournalEntryResponse>
    
    @PUT("api/journal/{entryId}/archive")
    suspend fun archiveJournalEntry(@Path("entryId") entryId: String): Response<String>
}
