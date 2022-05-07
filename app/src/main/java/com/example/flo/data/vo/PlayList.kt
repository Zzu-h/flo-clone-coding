package com.example.flo.data.vo

data class PlayList(
    var playList: ArrayList<Song>,
    var currentSong: Song,
    var index: Int = 0
)