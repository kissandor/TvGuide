package com.sandor.kis.tvguide.repository

import com.sandor.kis.tvguide.data.model.TVChannel
import com.sandor.kis.tvguide.network.ApiService
import java.util.ArrayList

class TVChannelRepo(private val apiService: ApiService) {
    suspend fun fetchAllChannels(): List<TVChannel> {
        val response = apiService.getChannels()
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList<TVChannel>()  // Ha a válasz nem sikeres, akkor üres lista
        }
    }
}