package com.example.flo.vo

import com.example.flo.R
import java.io.Serializable

data class Song(
    val title: String,
    val singer: String,
    var second: Int = 0,
    var playTime: Int = 0,
    var isPlaying: Boolean = false,
    var mills: Float = 0f,
    var music: String = "",
    val coverImg: Int = R.drawable.img_album_exp2,
    var path: String? = null,
    val isTitle: Boolean = true,
): Serializable
