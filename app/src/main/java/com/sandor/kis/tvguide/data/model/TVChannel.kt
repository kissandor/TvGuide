package com.sandor.kis.tvguide.data.model

data class TVChannel (
    val channelId: Int,
    val channelName: String,
    val channelDescription: String,
    val lcn: Int,
    val logoUrl: String,
    val tstv: Boolean
)