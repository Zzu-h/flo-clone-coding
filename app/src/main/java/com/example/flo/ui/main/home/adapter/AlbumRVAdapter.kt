package com.example.flo.ui.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.vo.Album
import com.example.flo.databinding.ItemAlbumBinding
import java.util.ArrayList

class AlbumRVAdapter(private val albumList: ArrayList<Album>) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>(){
    var onItemClick:(Album) -> Unit = {}
    var playAlbum:(Album) -> Unit = {}

    fun addItem(album: Album){
        albumList.add(album)
        notifyDataSetChanged()
    }
    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener { onItemClick(albumList[position]) }
        holder.binding.itemAlbumTitleTv.setOnClickListener { removeItem(position) }
        holder.binding.itemAlbumPlayImgIv.setOnClickListener { playAlbum(albumList[position]) }
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album){
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg)
        }
    }
}