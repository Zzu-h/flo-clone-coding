package com.example.flo.data.vo

import com.google.gson.annotations.SerializedName

data class TrackSong(
    @SerializedName("songIdx") val songIdx: Int,
    @SerializedName("singer") val singer: String,
    @SerializedName("title") val title:String,
    @SerializedName("isTitleSong") val isTitle : String
)