package com.example.flo.data.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flo.R
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class Album(
    @SerializedName("albumIdx") val albumIdx: Int,
    @SerializedName("singer") val singer: String,
    @SerializedName("title") val title:String,
    @SerializedName("coverImgUrl") val coverImgUrl : String,
)
/*
@Entity(tableName = "AlbumTable")
data class Album(
    val title: String = "Lilac",
    val singer: String = "IU",
    val coverImg: Int = R.drawable.img_album_exp2,
    var songIdList: String,
    @PrimaryKey(autoGenerate = true) var id : Int = 0,
)*/
