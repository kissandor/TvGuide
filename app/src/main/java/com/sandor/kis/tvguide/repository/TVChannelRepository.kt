package com.sandor.kis.tvguide.repository

import com.sandor.kis.tvguide.data.model.ChannelData
import com.sandor.kis.tvguide.data.model.TVChannel
import com.sandor.kis.tvguide.network.ApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class TVChannelRepository(private val apiService: ApiService) {
    suspend fun fetchAllChannelData(): List<List<ChannelData>> = coroutineScope {
        // Csatornák lekérése
        val channelsResponse = apiService.getChannels()
        val channels = if (channelsResponse.isSuccessful) {
            channelsResponse.body() ?: emptyList()
        } else {
            emptyList<TVChannel>()  // Ha a válasz nem sikeres, akkor üres lista
        }

        // Párhuzamos API hívások indítása
        val channelDataList = channels.map { channel ->
            async {
                // Aszinkron kérés egyes csatornák adatainak lekérésére
                val channelDataResponse = apiService.getChannelData(channel.channelid).body()
                channelDataResponse?.map { channelData ->
                    channelData.copy(
                        channelName = channel.channelname, // Csatorna neve
                        logoUrl = channel.logourl         // Csatorna logó URL-je#

                    )
                }

            }
        }

        // Várakozás az összes párhuzamos kérés befejeződésére és a válaszok összegyűjtése
        channelDataList.awaitAll().filterNotNull()
    }
}

