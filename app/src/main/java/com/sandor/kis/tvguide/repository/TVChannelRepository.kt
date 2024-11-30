/*package com.sandor.kis.tvguide.repository

import com.sandor.kis.tvguide.data.response.TVChannelResponse
import com.sandor.kis.tvguide.network.ApiService

class TVChannelRepository(private val apiService: ApiService) {
    suspend fun getChannels(): TVChannelResponse? {
        val response = apiService.getChannels()
        return if (response.isSuccessful) {
            response.body() // Visszaadjuk a választ, ha sikeres
        } else {
            // Hiba esetén null-t vagy hibakezelést adhatunk vissza
            null
        }
    }
}

 */