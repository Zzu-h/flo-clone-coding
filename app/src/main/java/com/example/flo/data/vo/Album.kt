package com.example.flo.data.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flo.R
import java.util.ArrayList

@Entity(tableName = "AlbumTable")
data class Album(
    val title: String = "Lilac",
    val singer: String = "IU",
    val coverImg: Int = R.drawable.img_album_exp2,
    var songIdList: String,
    @PrimaryKey(autoGenerate = true) var id : Int = 0,
)