package com.example.flo.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flo.data.dao.AlbumDao
import com.example.flo.data.dao.PlayListDao
import com.example.flo.data.dao.SongDao
import com.example.flo.data.vo.Album
import com.example.flo.data.vo.PlayList
import com.example.flo.data.vo.Song

@Database(entities = [Song::class, Album::class, PlayList::class], version = 1)
abstract class SongDatabase: RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun albumDao(): AlbumDao
    abstract fun playListDao(): PlayListDao

    companion object{
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if(instance == null){
                synchronized(SongDatabase::class.java){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database"
                    ).allowMainThreadQueries().build()
                }
            }
            return instance
        }
    }
}