package com.example.flo.ui.main.locker.save

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.flo.R
import com.example.flo.data.model.SongDatabase
import com.example.flo.ui.main.locker.save.adapter.SongRVAdapter
import com.example.flo.data.vo.Song
import com.example.flo.databinding.FragmentSaveSongBinding
import com.example.flo.ui.main.MainActivity

class SaveSongFragment : Fragment() {
    lateinit var binding: FragmentSaveSongBinding

    private var songList = ArrayList<Song>()
    lateinit var adapter: SongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveSongBinding.inflate(inflater, container, false)
        adapter = SongRVAdapter(songList).apply { onItemClick = this@SaveSongFragment::updateIsLike }
        binding.saveSongRv.adapter = adapter
        loadLikeSongs()
        return binding.root
    }

    private fun updateIsLike(song: Song)
        = SongDatabase.getInstance(this.requireContext())!!.songDao().updateIsLikeById(song.id, false)

    private fun loadLikeSongs(){
        val songDB = SongDatabase.getInstance(this.requireContext())!!
        songList = songDB.songDao().getLikedSong(true) as ArrayList<Song>
        songList.forEach{ adapter.addItem(it) }

        if(songList.isNotEmpty()) {
            adapter.notifyDataSetChanged()
            binding.noSongTv.isGone = true
            binding.saveSongRv.isVisible = true
        }
    }

    private val expList = arrayOf(
        R.drawable.img_album_exp,
        R.drawable.img_album_exp2,
        R.drawable.img_album_exp3,
        R.drawable.img_album_exp4,
        R.drawable.img_album_exp5,
    )
    private val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DATA
    )
    private fun loadLocalSongs(){
        val cursor: Cursor? = (context as MainActivity).contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, null
        )
        cursor?.apply {
            while (cursor.moveToNext()) {
                val musicDto = Song(
                    title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)),
                    singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)),
                    coverImg = expList.random()
                )
                println(musicDto)
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                songList.add(musicDto)
            }
            binding.noSongTv.isGone = true
            cursor.close()
            binding.saveSongRv.isVisible = true
        }
    }
}