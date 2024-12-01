package com.sandor.kis.tvguide.data.model

data class Tstv(
    val availabilityEnd: Int,
    val availabilityStart: Int,
    val mediaAvailable: Boolean,
    val mediaLocation: String,
    val sid: Int
)