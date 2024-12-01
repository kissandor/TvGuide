package com.sandor.kis.tvguide.data.model

data class ChannelData(
    val channelid: Int,
    val event: List<Event>,
    val offset: Int,
    val channelName: String,
    val logoUrl: String
)