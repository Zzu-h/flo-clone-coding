package com.example.flo

import java.io.Serializable

data class Song(
    val title: String,
    val singer: String,
    var second: Int = 0,
    var playTime: Int = 0,
    var isPlaying: Boolean = false,
    var mills: Float = 0f,
    var music: String = "",
): Serializable
