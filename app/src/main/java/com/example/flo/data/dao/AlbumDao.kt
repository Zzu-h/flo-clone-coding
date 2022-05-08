package com.example.flo.data.dao

import androidx.room.*
import com.example.flo.data.vo.Album
import com.example.flo.data.vo.Song

@Dao
interface AlbumDao {
    @Insert
    fun insert(album: Album)

    @Update
    fun update(album: Album)

    @Delete
    fun delete(album: Album)

    @Query("SELECT * FROM AlbumTable")
    fun getAlbums(): List<Album>

    @Query("SELECT * FROM AlbumTable WHERE id = :albumId")
    fun getAlbum(albumId: Int): Album
}