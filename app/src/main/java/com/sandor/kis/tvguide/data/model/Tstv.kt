package com.sandor.kis.tvguide.data.model

data class Tstv(
    val availabilityEnd: Long,
    val availabilityStart: Long,
    val mediaAvailable: Boolean,
    val mediaLocation: String,
    val sid: Int
)