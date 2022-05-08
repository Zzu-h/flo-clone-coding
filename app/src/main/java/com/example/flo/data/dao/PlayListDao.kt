package com.example.flo.data.dao

import androidx.room.*
import com.example.flo.data.vo.PlayList
import com.example.flo.data.vo.Song

@Dao
interface PlayListDao {
    @Insert
    fun insert(playList: PlayList)

    @Update
    fun update(playList: PlayList)

    @Delete
    fun delete(playList: PlayList)

    @Query("SELECT songIdList FROM PlayList WHERE playListName = :name")
    fun getPlayList(name: String): String

    @Query("DELETE FROM PlayList WHERE playListName = :name")
    fun deletePlayList(name: String)
}