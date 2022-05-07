package com.example.flo.data.dao

import androidx.room.*
import com.example.flo.data.vo.Song

@Dao
interface SongDao {
    @Insert
    fun insert(song: Song)

    @Update
    fun update(song: Song)

    @Delete
    fun delete(song: Song)

    @Query("SELECT * FROM SongTable")
    fun getSongs(): List<Song>

    @Query("SELECT * FROM SongTable WHERE id = :id")
    fun getSong(id: Int): Song

    @Query("UPDATE SongTable SET isLike = :isLike WHERE id = :id")
    fun updateIsLikeById(id: Int, isLike: Boolean )

    @Query("SELECT * FROM SongTable WHERE isLike = :isLike")
    fun getLikedSong(isLike: Boolean): List<Song>
}