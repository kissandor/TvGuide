package com.sandor.kis.tvguide.data.model

data class TVChannel (
    val channelid: Int,
    val channelname: String,
    val channeldescription: String,
    val lcn: Int,
    val logourl: String,
    val tstv: Boolean
)