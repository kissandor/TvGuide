package com.sandor.kis.tvguide.network

import com.sandor.kis.tvguide.data.model.TVChannel
import com.sandor.kis.tvguide.data.response.TVChannelResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("api")
    suspend fun getChannels(): Response<List<TVChannel>>
}