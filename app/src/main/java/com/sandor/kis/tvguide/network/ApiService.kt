package com.sandor.kis.tvguide.network

import com.sandor.kis.tvguide.data.model.ChannelData
import com.sandor.kis.tvguide.data.model.TVChannel
import retrofit2.Response
import retrofit2.http.GET
import kotlinx.coroutines.*
import retrofit2.http.Query

interface ApiService {
    @GET("api")
    suspend fun getChannels(): Response<List<TVChannel>>

    @GET("api/0")
    suspend fun getChannelData(@Query ("channel") channelId: Int): Response<List<ChannelData>>
}

