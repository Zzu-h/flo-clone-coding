package com.example.flo.ui.album.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flo.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding

    private var albumTitle = "Lilac"
    private var albumInfo =
        """
            |'아이유(IU)' 정규 5집 (LILAC)
            |
            |"안녕 꽃입 같은 안녕"
            |...
        """.trimMargin()
    private var albumArtist = "아이유 (IU)"
    private var publisher = "카카오엔터테인먼트"
    private var agency = "EDAM엔터테인먼트"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        binding.albumTitleTv.text = albumTitle
        binding.albumInfoTv.text = albumInfo
        binding.albumArtistTv.text = albumArtist
        binding.publisherTv.text = publisher
        binding.agencyTv.text = agency

        return binding.root
    }
}