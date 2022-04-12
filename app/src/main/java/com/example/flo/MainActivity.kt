package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var song: Song
    private var timer: MainActivity.Timer? = null
    private val aniDuration = 300L

    private val gson = Gson()
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_FLO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClickListener()

        initBottomNavigation()
        binding.mainPlayerCl.isClickable = true
        binding.mainPlayerCl.setOnClickListener { startActivity(Intent(this, SongActivity::class.java)) }
        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("song", song)
            startActivity(intent)
        }
    }
    override fun onStart() {
        super.onStart()
        val sharedPreference = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreference.getString("songData", null)

        song = if(songJson != null) gson.fromJson(songJson, Song::class.java)
            else Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(),0,60, false, 0f, "music_lilac")

        setMiniPlayer(song)
    }
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        val sharedPreference = getSharedPreferences("song", MODE_PRIVATE)
        val edit = sharedPreference.edit()

        edit.putString("songData", gson.toJson(song))
        edit.apply()
    }
    override fun onDestroy() {
        super.onDestroy()
        timer?.interrupt()
        mediaPlayer?.release()
    }

    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
    private fun startTimer(){
        timer = Timer(song.playTime, song.isPlaying, song.second, song.mills)
        timer?.start()
    }
    private fun stopTimer() {
        if(timer == null) return
        timer?.timerStop()
    }

    private fun setMiniPlayer(song: Song){
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.songProgressSb.progress = (song.second * 100000 / song.playTime)

        setPlayerStatus(song.isPlaying)
    }
    private fun initClickListener(){
        binding.mainMiniplayerBtn.setOnClickListener { setPlayerStatus(true) }
        binding.mainPauseBtn.setOnClickListener { setPlayerStatus(false) }
        binding.songNextIv.setOnClickListener { aniBigSmall(binding.songNextIv) }
        binding.songPreviousIv.setOnClickListener { aniBigSmall(binding.songPreviousIv) }
    }
    private fun setPlayerStatus (isPlaying : Boolean){
        song.isPlaying = isPlaying

        if(isPlaying){
            startTimer()

            binding.mainPauseBtn.animate().apply {
                rotationY(-90f)
            }.start()
            binding.mainMiniplayerBtn.animate().apply {
                duration = aniDuration
                rotationYBy(90f)
            }.withEndAction {
                binding.mainMiniplayerBtn.visibility = View.GONE
                binding.mainPauseBtn.visibility = View.VISIBLE
                binding.mainPauseBtn.animate().apply {
                    duration = aniDuration
                    rotationYBy(90f)
                }.start()
            }
            binding.mainMiniplayerBtn.animate().apply {
                rotationYBy(-90f)
            }.start()

            mediaPlayer?.apply {
                this.seekTo(song.mills.toInt())
                start()
            }
        } else {
            stopTimer()

            binding.mainMiniplayerBtn.animate().apply {
                rotationY(-90f)
            }.start()
            binding.mainPauseBtn.animate().apply {
                duration = aniDuration
                rotationYBy(90f)
            }.withEndAction {
                binding.mainMiniplayerBtn.visibility = View.VISIBLE
                binding.mainPauseBtn.visibility = View.GONE
                binding.mainMiniplayerBtn.animate().apply {
                    duration = aniDuration
                    rotationYBy(90f)
                }.start()
            }
            binding.mainPauseBtn.animate().apply {
                rotationYBy(-90f)
            }.start()

            if(mediaPlayer?.isPlaying == true)
                mediaPlayer?.pause()
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

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true, private var second: Int = 0, private var mills: Float = 0f): Thread(){

        private var runningFlag = true

        override fun run() {
            super.run()
            try {
                runningFlag = true
                while (runningFlag){
                    if(second >= playTime) {
                        song.mills = 0f
                        song.second = 0
                        this@MainActivity.setPlayerStatus(false)
                        break
                    }
                    if(isPlaying){
                        sleep(50)
                        mills += 50
                        song.mills = mills
                        runOnUiThread { binding.songProgressSb.progress = ((mills / playTime)*100).toInt() }
                        if(mills % 1000 == 0f){
                            second++
                            song.second = second
                        }
                    }
                }
            } catch (e: Exception){ }
        }
        fun timerStop() { runningFlag = false }
    }
}