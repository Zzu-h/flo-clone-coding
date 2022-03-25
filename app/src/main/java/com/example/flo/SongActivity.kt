package com.example.flo

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    //전역 변수
    lateinit var binding : ActivitySongBinding
    private val aniDuration = 300L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(intent.hasExtra("title")&&intent.hasExtra("singer")){
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }

        initClickListener()
    }

    private fun initClickListener(){
        binding.songDownIb.setOnClickListener { finish() }
        binding.songMiniplayerIv.setOnClickListener { setPlayerStatus(true) }
        binding.songPauseIv.setOnClickListener { setPlayerStatus(false) }
        binding.songNextIv.setOnClickListener { aniBigSmall(binding.songNextIv) }
        binding.songPreviousIv.setOnClickListener { aniBigSmall(binding.songPreviousIv) }
    }

    private fun setPlayerStatus (isPlaying : Boolean){
        if(isPlaying){
            binding.songPauseIv.animate().apply {
                rotationY(-90f)
            }.start()
            binding.songMiniplayerIv.animate().apply {
                duration = aniDuration
                rotationYBy(90f)
            }.withEndAction {
                binding.songMiniplayerIv.visibility = View.GONE
                binding.songPauseIv.visibility = View.VISIBLE
                binding.songPauseIv.animate().apply {
                    duration = aniDuration
                    rotationYBy(90f)
                }.start()
            }
            binding.songMiniplayerIv.animate().apply {
                rotationYBy(-90f)
            }.start()
        } else {
            binding.songMiniplayerIv.animate().apply {
                rotationY(-90f)
            }.start()
            binding.songPauseIv.animate().apply {
                duration = aniDuration
                rotationYBy(90f)
            }.withEndAction {
                binding.songMiniplayerIv.visibility = View.VISIBLE
                binding.songPauseIv.visibility = View.GONE
                binding.songMiniplayerIv.animate().apply {
                    duration = aniDuration
                    rotationYBy(90f)
                }.start()
            }
            binding.songPauseIv.animate().apply {
                rotationYBy(-90f)
            }.start()

        }
    }
    private fun aniBigSmall(view: ImageView){
        val xSize = view.scaleX
        val ySize = view.scaleY

        view.animate().apply {
            duration = aniDuration
            this.scaleX(1.2f)
            this.scaleY(1.2f)
        }.withEndAction {
            view.animate().apply{
                duration = aniDuration
                this.scaleX(xSize)
                this.scaleY(ySize)
            }.start()
        }
    }
}