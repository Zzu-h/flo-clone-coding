package com.example.flo.data.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flo.R
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class BsideTracks(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: List<TrackSong>
)