package com.example.flo.data.vo

import com.google.gson.annotations.SerializedName

data class SongResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: FloChartResult
)