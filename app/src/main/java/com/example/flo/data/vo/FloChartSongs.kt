package com.example.flo.data.vo

import com.google.gson.annotations.SerializedName

data class FloChartSongs(
    @SerializedName("songIdx") val songIdx: Int,
    @SerializedName("albumIdx") val albumIdx: Int,
    @SerializedName("singer") val singer: String,
    @SerializedName("title") val title:String,
    @SerializedName("coverImgUrl") val coverImgUrl : String
)