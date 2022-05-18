package com.example.flo.ui.album.song

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.flo.data.datasource.AlbumNetworkDataSource
import com.example.flo.data.model.SongCode
import com.example.flo.data.model.Converter
import com.example.flo.data.model.SongDatabase
import com.example.flo.ui.album.song.adapter.TrackRVAdapter
import com.example.flo.databinding.FragmentSongBinding
import com.example.flo.data.vo.Album
import com.example.flo.data.vo.Song
import com.example.flo.data.vo.TrackSong
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

        /*val gson = Gson()
        val album = gson.fromJson(arguments?.getString(SongCode.album), Album::class.java)
        Toast.makeText(this.requireContext(), album.toString(), Toast.LENGTH_SHORT).show()
        album?.apply {
            val songDB = SongDatabase.getInstance(this@SongFragment.requireContext())!!
            val trackIdList = Converter.stringToList(album.songIdList)

            trackIdList.forEach { trackList.add(songDB.songDao().getSong(it)) }

            binding.songMusicListRv.adapter = TrackRVAdapter(trackList)
        }*/
        val idx = arguments?.getInt("id")
        idx?.apply {
            if(idx == 0) return@apply

            AlbumNetworkDataSource().getBsideTracks(idx,object : AlbumNetworkDataSource.BsideTrackInterface{
                override fun onLoadFailure(message: String?) {
                    Toast.makeText(this@SongFragment.requireContext(),message.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onLoadSuccess(songList: List<TrackSong>) {
                    binding.songMusicListRv.adapter = TrackRVAdapter(songList as ArrayList<TrackSong>)
                }
            })
        }
        return binding.root
    }
}