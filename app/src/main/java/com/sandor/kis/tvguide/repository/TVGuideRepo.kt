package com.sandor.kis.tvguide.repository

import com.sandor.kis.tvguide.data.model.ChannelData
import com.sandor.kis.tvguide.network.ApiService
import com.sandor.kis.tvguide.network.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope


class TVGuideRepo(private val apiService: ApiService) {

    // Aszinkron lekérés
    suspend fun fetchAllChannelInfo(): List<ChannelData> = coroutineScope {
        val fetchedTvChannels = TVChannelRepo(RetrofitClient.retrofit).fetchAllChannels()

        // Az összes csatornához aszinkron módon kérjük le az adatokat
        val fetchedChannelDataList = fetchedTvChannels.map { fetchedTvChannel ->
            async {
                try {
                    val channelId = fetchedTvChannel.channelid

                        val fetchedChannelDataResponse = apiService.getChannelData(channelId.toInt()).body()
                        fetchedChannelDataResponse?.map { channelData ->
                            channelData.copy(
                                channelName = fetchedTvChannel.channelname,  // Csatorna neve
                                logoUrl = fetchedTvChannel.logourl         // Csatorna logó URL-je
                            )
                        }
                } catch (e: Exception) {
                    android.util.Log.e("TVGuideRepo", "Error fetching data for channel: ${fetchedTvChannel.channelid}", e)
                    null
                }
            }
        }

        // Az összes aszinkron kérés végrehajtása és eredmények összegyűjtése
        val channelDataResults = fetchedChannelDataList.awaitAll()

        // Kiválogatjuk az érvényes ChannelData elemeket
        //return@coroutineScope channelDataResults.filterNotNull().flatMap{}

        val flatChannelDataList = mutableListOf<ChannelData>()

        for (channelDataList in channelDataResults) {
            channelDataList?.let {
                flatChannelDataList.addAll(it)  // Csak akkor adja hozzá, ha nem null
            }
        }

        return@coroutineScope flatChannelDataList
    }
}