package com.example.flo.ui.main.locker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.flo.data.model.SongDatabase
import com.example.flo.data.model.UserCode.auth
import com.example.flo.data.model.UserCode.jwt
import com.example.flo.databinding.FragmentLockerBinding
import com.example.flo.ui.main.MainActivity
import com.example.flo.ui.user.login.LoginActivity
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    private val information = arrayListOf("저장한 곡", "음악파일", "저장앨범")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerVPAdapter(this)
        binding.lockerVp.adapter = lockerAdapter
        TabLayoutMediator(binding.lockerTb, binding.lockerVp){ tab, position -> tab.text = information[position] }
            .attach()

        binding.lockerLoginTv.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        /*val likedAlbums = songDB.albumDao().getLikedAlbums(userId)
        Log.d("LOKERFRAG/GET_ALBUMS", likedAlbums.toString())*/

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initViews()
    }

    private fun initViews() {
        val jwt: Int = getJwt()

        if (jwt == 0){
            binding.lockerLoginTv.text = "로그인"
            binding.lockerLoginTv.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
        else{
            val user = SongDatabase
                .getInstance(this.requireContext())!!
                .userDao()
                .getUserById(jwt)
            user?.apply {
                binding.lockerLoginTv.text = "로그아웃"
                binding.userName.text = user.name
                binding.userName.isVisible = true
                binding.lockerLoginTv.setOnClickListener {
                    logout()
                    startActivity(Intent(activity, MainActivity::class.java))
                }
            }
        }
    }

    private fun getJwt(): Int
        = activity?.getSharedPreferences(auth , AppCompatActivity.MODE_PRIVATE)!!.getInt(jwt, 0)

    private fun logout() {
        val spf = activity?.getSharedPreferences(auth , AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()

        editor.remove(jwt)
        editor.apply()
        binding.userName.isGone = true
    }
}