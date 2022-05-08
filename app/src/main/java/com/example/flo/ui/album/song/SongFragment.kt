package com.example.flo.ui.album.song

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flo.data.model.CODE
import com.example.flo.data.model.Converter
import com.example.flo.data.model.SongDatabase
import com.example.flo.ui.album.song.adapter.TrackRVAdapter
import com.example.flo.databinding.FragmentSongBinding
import com.example.flo.data.vo.Album
import com.example.flo.data.vo.Song
import com.google.gson.Gson
import java.util.ArrayList

class SongFragment : Fragment() {
    lateinit var binding: FragmentSongBinding
    private var trackList = ArrayList<Song>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        val gson = Gson()
        val album = gson.fromJson(arguments?.getString(CODE.album), Album::class.java)
        album?.apply {
            val songDB = SongDatabase.getInstance(this@SongFragment.requireContext())!!
            val trackIdList = Converter.stringToList(album.songIdList)

            trackIdList.forEach { trackList.add(songDB.songDao().getSong(it)) }

            binding.songMusicListRv.adapter = TrackRVAdapter(trackList)
        }
        return binding.root
    }
}