package com.example.flo.ui.main.home.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    private val fragmentList: ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int = fragmentList.size
    override fun createFragment(position: Int): Fragment  = fragmentList[position]

    fun addFragment(fragment: Fragment)
        = fragmentList.add(fragment).apply { notifyItemChanged(fragmentList.lastIndex) }
}