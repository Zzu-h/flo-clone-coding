package com.example.flo.data.vo

import com.google.gson.annotations.SerializedName

data class FloChartResult(
    @SerializedName("songs") val songs: List<FloChartSongs>
)