package com.example.flo.ui.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flo.R
import com.example.flo.data.vo.Album
import com.example.flo.databinding.FragmentAlbumBinding
import com.example.flo.ui.main.CODE
import com.example.flo.ui.main.home.HomeFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {
    lateinit var binding: FragmentAlbumBinding
    private val information = arrayListOf("수록곡","상세정보","영상")
    private var gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(layoutInflater, container, false)
        binding.albumBackIv.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }
        val album = gson.fromJson(arguments?.getString(CODE.album), Album::class.java)
        album?.apply {
            binding.albumAlbumIv.setImageResource(album.coverImg)
            binding.albumMusicTitleTv.text = album.title
            binding.albumSingerNameTv.text = album.singer
        }

        val albumAdapter = AlbumVPAdapter(this)
            .apply {
                songFragment.arguments = Bundle().apply {
                    val albumJson = gson.toJson(album)
                    putString(CODE.album, albumJson)
                }
            }
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){ tab, position -> tab.text = information[position] }
            .attach()

        return binding.root
    }
}