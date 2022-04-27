package com.example.flo

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.vo.Song
import com.example.flo.databinding.ActivitySongBinding
import com.example.flo.vo.PlayList
import com.google.gson.Gson
import java.lang.Exception

class SongActivity : AppCompatActivity() {

    //전역 변수
    lateinit var binding : ActivitySongBinding
    lateinit var song: Song
    lateinit var playList: PlayList
    private var timer: Timer? = null
    private val aniDuration = 300L

    private val gson = Gson()
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(intent.hasExtra(CODE.play_list)) {
            val playListJson = intent.getStringExtra(CODE.play_list)
            playList = gson.fromJson(playListJson, PlayList::class.java)
        }
        else finish()
        song = playList.currentSong
        setPlayer(song)

        initClickListener()
    }
    private fun startTimer(){
        timer = Timer(song.playTime, song.isPlaying, song.second, song.mills)
        timer?.start()
    }
    private fun stopTimer() {
        if(timer == null) return
        timer?.interrupt()
    }
    private fun prevMusic(){
        val size = playList.playList.size
        playList.index = ((playList.index - 1) + size).mod(size)
        playList.currentSong = playList.playList[playList.index]
        playList.currentSong.isPlaying = true
        setPlayer(playList.currentSong)
    }
    private fun nextMusic(){
        val size = playList.playList.size
        playList.index = (playList.index + 1).mod(size)
        playList.currentSong = playList.playList[playList.index]
        playList.currentSong.isPlaying = true
        setPlayer(playList.currentSong)
    }

    private fun setPlayer(playerSong: Song){
        mediaPlayer?.apply {
            stopTimer()
            if(mediaPlayer!!.isPlaying) mediaPlayer!!.stop()
            mediaPlayer = null
            song = playerSong
            song.mills = 0f
            song.second = 0
        }
        val music = resources.getIdentifier(playerSong.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        mediaPlayer?.apply { playerSong.playTime = this.duration/1000 }

        binding.songMusicTitleTv.text = playerSong.title
        binding.songSingerNameTv.text = playerSong.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d", playerSong.second / 60, playerSong.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", playerSong.playTime / 60, playerSong.playTime % 60)
        binding.songProgressSb.progress = (playerSong.second * 100000 / playerSong.playTime)

        setPlayerStatus(playerSong.isPlaying)
    }
    private fun initClickListener(){
        binding.songDownIb.setOnClickListener { finish() }
        binding.songMiniplayerIv.setOnClickListener { setPlayerStatus(true) }
        binding.songPauseIv.setOnClickListener { setPlayerStatus(false) }
        binding.songNextIv.setOnClickListener {
            aniBigSmall(binding.songNextIv)
            nextMusic()
        }
        binding.songPreviousIv.setOnClickListener {
            aniBigSmall(binding.songPreviousIv)
            prevMusic()
        }
    }

    // 사용자가 포커스를 잃었을 때 음악 중지
    override fun onPause() {
        super.onPause()
        //setPlayerStatus(false)
        val sharedPreference = getSharedPreferences(CODE.music, MODE_PRIVATE)
        val edit = sharedPreference.edit()

        edit.putString(CODE.play_list, gson.toJson(playList))
        edit.apply()
    }
    override fun onDestroy() {
        super.onDestroy()
        timer?.interrupt()
        mediaPlayer?.release()
    }

    private fun setPlayerStatus (isPlaying : Boolean){
        song.isPlaying = isPlaying
        //timer.isPlaying = isPlaying

        if(isPlaying){
            startTimer()

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
            mediaPlayer?.apply {
                this.seekTo(song.mills.toInt())
                start()
            }
        } else {
            stopTimer()

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
                        this@SongActivity.nextMusic()
                        this@SongActivity.setPlayerStatus(false)
                        break
                    }
                    if(isPlaying){
                        sleep(50)
                        mills += 50
                        song.mills = mills
                        runOnUiThread { binding.songProgressSb.progress = ((mills / playTime)*100).toInt() }
                        if(mills % 1000 == 0f)
                            runOnUiThread { binding.songStartTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)}
                                .apply {
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