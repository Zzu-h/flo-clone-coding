package com.example.flo.vo

data class PlayList(
    var playList: ArrayList<Song>,
    var currentSong: Song,
    var index: Int = 0
)