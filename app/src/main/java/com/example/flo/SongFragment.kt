package com.example.flo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import com.example.flo.databinding.FragmentSongBinding
import com.example.flo.databinding.ItemAlbumSongBinding

class SongFragment : Fragment() {
    lateinit var binding: FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)
        setItemText()

        binding.songLalacLayout.root.isClickable = false
        binding.songLalacLayout.root.setOnClickListener { Toast.makeText(activity,"LILAC", Toast.LENGTH_SHORT).show() }

        return binding.root
    }

    private fun setItemText(){
        binding.songLalacLayout
        setItemAlbumSongText(binding.songLalacLayout, 1, "라일락", false, "가수")
        setItemAlbumSongText(binding.songFluLayout, 2, "Flu", true, "가수")
        setItemAlbumSongText(binding.songCoinLayout, 3, "Coin", false, "가수")
        setItemAlbumSongText(binding.songSpringLayout, 4, "봄 안녕 봄", true, "가수")
    }

    private fun setItemAlbumSongText(view: ItemAlbumSongBinding, index: Int, title: String, isNotTitle: Boolean, singer: String){
        view.itemSongTitleTv.text = title
        view.isTitleTv.isGone = isNotTitle
        view.itemSongSingerTv.text = singer
        view.itemSongIndex.text = "0$index"
    }
}