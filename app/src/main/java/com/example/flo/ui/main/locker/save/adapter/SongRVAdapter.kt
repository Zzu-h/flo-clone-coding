package com.example.flo.ui.main.locker.save.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.vo.Song
import com.example.flo.databinding.ItemSongBinding

class SongRVAdapter(private val albumList: ArrayList<Song>) : RecyclerView.Adapter<SongRVAdapter.ViewHolder>(){
    var onItemClick:(Song) -> Unit = {}

    fun addItem(album: Song){
        albumList.add(album)
        notifyDataSetChanged()
    }
    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(ItemSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.binding.itemSongMoreIv.setOnClickListener { removeItem(position) }
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemSongBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album: Song){
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg)
            binding.itemSongTitleTv.text = album.title
            binding.itemSongSingerTv.text = album.singer
        }
    }
}