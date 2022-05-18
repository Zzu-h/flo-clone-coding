package com.example.flo.ui.album.song.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumSongBinding
import com.example.flo.data.vo.Song
import com.example.flo.data.vo.TrackSong
import java.util.ArrayList

class TrackRVAdapter(private val trackList: ArrayList<TrackSong>) : RecyclerView.Adapter<TrackRVAdapter.ViewHolder>(){
    var onItemClick:(TrackSong) -> Unit = {}

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(ItemAlbumSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener { onItemClick(trackList[position]) }
        holder.binding.itemSongIndex.text = "0${position+1}"
    }

    override fun getItemCount(): Int = trackList.size

    inner class ViewHolder(val binding: ItemAlbumSongBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(song: TrackSong){
            binding.itemSongTitleTv.text = song.title
            binding.itemSongSingerTv.text = song.singer
            binding.isTitleTv.isGone = (song.isTitle == "F")
        }
    }
}