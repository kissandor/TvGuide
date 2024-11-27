package com.sandor.kis.tvguide.data.response

import com.sandor.kis.tvguide.data.model.TVChannel

data class TVGuideResponse(
    val channels: List<TVChannel>
)