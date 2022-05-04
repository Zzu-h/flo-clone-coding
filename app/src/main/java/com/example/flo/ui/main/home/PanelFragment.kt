package com.example.flo.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flo.R
import com.example.flo.databinding.FragmentHomePannelBinding
import com.example.flo.ui.album.AlbumFragment

class PanelFragment(private val imgRes: Int = R.drawable.img_first_album_default) : Fragment() {
    lateinit var binding: FragmentHomePannelBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePannelBinding.inflate(inflater, container, false)
        binding.pannelImageIv.setImageResource(imgRes)

        binding.homePannelAlbumImg01Iv.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        return binding.root
    }
}