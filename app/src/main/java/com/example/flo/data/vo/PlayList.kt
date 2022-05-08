package com.example.flo.data.vo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PlayList")
data class PlayList(
    var songIdList: String,
    var playListName: String,
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
)