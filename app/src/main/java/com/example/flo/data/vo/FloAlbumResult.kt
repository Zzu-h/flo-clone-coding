package com.example.flo.data.vo

import com.google.gson.annotations.SerializedName

data class FloAlbumResult(
    @SerializedName("albums") val songs: List<Album>
)