package com.example.flo.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.DetailFragment
import com.example.flo.SongFragment
import com.example.flo.VideoFragment

class AlbumVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    val songFragment = SongFragment()
    val detailFragment = DetailFragment()
    val videoFragment = VideoFragment()
    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment
        = when(position){
            0 -> songFragment
            1 -> detailFragment
            else -> videoFragment
        }
}