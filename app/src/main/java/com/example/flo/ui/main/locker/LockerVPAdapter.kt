package com.example.flo.ui.main.locker

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.ui.main.locker.music.MusicFileFragment
import com.example.flo.ui.main.locker.save.SaveSongFragment

class LockerVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment
        = when(position){
            0 -> SaveSongFragment()
            else -> MusicFileFragment()
        }
}