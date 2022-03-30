package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PanelVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    private val fragmentList: ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int = fragmentList.size
    override fun createFragment(position: Int): Fragment  = fragmentList[position]

    fun addFragment(fragment: Fragment)
        = fragmentList.add(fragment).apply { notifyItemChanged(fragmentList.lastIndex) }
}