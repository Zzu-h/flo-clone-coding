package com.example.flo.data.vo

import com.example.flo.R
import java.util.ArrayList

data class Album(
    val title: String = "Lilac",
    val singer: String = "IU",
    val coverImg: Int = R.drawable.img_album_exp2,
    var songs: ArrayList<Song>? = null,
)