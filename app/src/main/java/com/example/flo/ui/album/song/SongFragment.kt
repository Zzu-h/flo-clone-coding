package com.example.flo.ui.album.song

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flo.ui.album.song.adapter.TrackRVAdapter
import com.example.flo.databinding.FragmentSongBinding
import com.example.flo.ui.main.CODE
import com.example.flo.data.vo.Album
import com.google.gson.Gson

class SongFragment : Fragment() {
    lateinit var binding: FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)
        //setItemText()

        val gson = Gson()
        val album = gson.fromJson(arguments?.getString(CODE.album), Album::class.java)
        Log.d("Song", "test1")
        album?.apply {
            Log.d("Song", "test2")
            if(album.songs != null)
                binding.songMusicListRv.adapter = TrackRVAdapter(album.songs!!)
            Log.d("Song", "test3")
        }
        Log.d("Song", "test4")
        return binding.root
    }

    /*private fun setItemText(){
        binding.songLalacLayout
        setItemAlbumSongText(binding.songLalacLayout, 1, "라일락", false, "가수")
        setItemAlbumSongText(binding.songFluLayout, 2, "Flu", true, "가수")
        setItemAlbumSongText(binding.songCoinLayout, 3, "Coin", false, "가수")
        setItemAlbumSongText(binding.songSpringLayout, 4, "봄 안녕 봄", true, "가수")
    }*/

    /*private fun setItemAlbumSongText(view: ItemAlbumSongBinding, index: Int, title: String, isNotTitle: Boolean, singer: String){
        view.itemSongTitleTv.text = title
        view.isTitleTv.isGone = isNotTitle
        view.itemSongSingerTv.text = singer
        view.itemSongIndex.text = "0$index"
    }*/
}