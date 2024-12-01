package com.sandor.kis.tvguide.data.model

data class Event(
    val description: String,
    val duration: Int,
    val episodeNo: Int,
    val evtId: Int,
    val hasTstv: Boolean,
    val image: String,
    val name: String,
    val seriesNo: Int,
    val startTime: Int,
    val svcId: Int,
    val tstv: Tstv
)