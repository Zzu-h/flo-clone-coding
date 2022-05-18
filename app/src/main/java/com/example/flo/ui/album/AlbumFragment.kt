package com.example.flo.ui.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.flo.R
import com.example.flo.data.model.SongCode
import com.example.flo.data.model.SongDatabase
import com.example.flo.data.model.UserCode.auth
import com.example.flo.data.model.UserCode.jwt
import com.example.flo.data.vo.Album
import com.example.flo.data.vo.Like
import com.example.flo.databinding.FragmentAlbumBinding
import com.example.flo.ui.main.MainActivity
import com.example.flo.ui.main.home.HomeFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {
    private lateinit var binding: FragmentAlbumBinding
    private val information = arrayListOf("수록곡", "상세정보", "영상")

    private var isLiked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumData = arguments?.getString("album")
        val gson = Gson()

        val album = gson.fromJson(albumData, Album::class.java)
        isLiked = isLikedAlbum(album.albumIdx)

        setViews(album)
        initViewPager(album.albumIdx)
        setClickListeners(album)

        return binding.root
    }

    private fun setViews(album: Album) {
        binding.albumMusicTitleTv.text = album.title.toString()
        binding.albumSingerNameTv.text = album.singer.toString()
        //binding.albumAlbumIv.setImageResource(album.coverImg)
        Glide.with(this)
            .load(album.coverImgUrl)
            .into(binding.albumAlbumIv)

        binding.albumLikeIv.setImageResource(
            if(isLiked) R.drawable.ic_my_like_on
            else R.drawable.ic_my_like_off
        )

    }

    private fun setClickListeners(album: Album) {
        val userId: Int = getJwt()

        binding.albumLikeIv.setOnClickListener {
            if(isLiked) {
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
                disLikeAlbum(userId, album.albumIdx)
            } else {
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.albumIdx)
            }

            isLiked = !isLiked
        }

        //set click listener
        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }
    }

    private fun initViewPager(albumId: Int) {
        //init viewpager
        val albumAdapter = AlbumVPAdapter(this)
        albumAdapter.songFragment.apply {
            arguments = Bundle().apply { putInt("id", albumId) }
        }

        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()
    }

    private fun disLikeAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        //songDB.albumDao().disLikeAlbum(userId, albumId)
    }

    private fun likeAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        //songDB.albumDao().likeAlbum(like)
    }

    private fun isLikedAlbum(albumId: Int): Boolean {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        //val likeId: Int? = songDB.albumDao().isLikedAlbum(userId, albumId)

        return false//likeId != null
    }

    private fun getJwt(): Int {
        val spf = activity?.getSharedPreferences(auth, AppCompatActivity.MODE_PRIVATE)
        val jwt = spf!!.getInt(jwt, 0)
        Log.d("MAIN_ACT/GET_JWT", "jwt_token: $jwt")

        return jwt
    }
}