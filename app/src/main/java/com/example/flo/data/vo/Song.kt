package com.example.flo.data.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flo.R
import java.io.Serializable

@Entity(tableName = "SongTable")
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
    var isLike: Boolean = false,
): Serializable {
    @PrimaryKey(autoGenerate = true) var id = 0
}
