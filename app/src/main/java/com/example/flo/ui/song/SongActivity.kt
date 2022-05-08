package com.example.flo.ui.song

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.R
import com.example.flo.data.model.CODE
import com.example.flo.data.model.Converter
import com.example.flo.data.model.SongDatabase
import com.example.flo.data.model.Timer
import com.example.flo.data.vo.Song
import com.example.flo.databinding.ActivitySongBinding
import com.example.flo.data.vo.PlayList
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    //전역 변수
    lateinit var binding : ActivitySongBinding
    lateinit var song: Song
    lateinit var playList: MutableList<Song>
    private var playListSize = 0
    private var playListPos = 0

    private var timer: Timer? = null
    private val aniDuration = 300L

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        playList = mutableListOf<Song>()
        setContentView(binding.root)
        initialize()

        initClickListener()
    }
    private fun initialize(){
        if(!intent.hasExtra(CODE.playingSongID)) {
            finish()
            return
        }

        val sharedPreference = getSharedPreferences(CODE.music, MODE_PRIVATE)
        val songId = sharedPreference.getInt(CODE.playingSongID,0)

        val songDB = SongDatabase.getInstance(this)!!
        val dbPlayList = songDB.playListDao().getPlayList(CODE.currentPlayList)
        val songIdList = Converter.stringToList(dbPlayList)

        songIdList.forEach { playList.add(songDB.songDao().getSong(it)) }

        if(songId != 0) playList.forEachIndexed { index, c -> if(songId == c.id) playListPos = index }
        else playListPos = 0

        playListSize = songIdList.size
        song = playList[playListPos]

        setPlayer(song)
    }

    private fun startTimer(){
        timer = Timer(this, song, song.playTime, song.isPlaying, song.second, song.mills)
            .apply {
                nextMusic = this@SongActivity::nextMusic
                setPlayerStatus = this@SongActivity::setPlayerStatus
                updateProgressSb = this@SongActivity::updateProgressSb
            }
        timer?.start()
    }
    private fun stopTimer() {
        if(timer == null) return
        timer?.interrupt()
    }
    private fun updateProgressSb(time: Int) { binding.songProgressSb.progress = time }
    private fun prevMusic(){
        playListPos = ((playListPos - 1) + playListSize).mod(playListSize)
        song = playList[playListPos]

        setPlayer(song)
    }
    private fun nextMusic(){
        playListPos = (playListPos + 1).mod(playListSize)
        song = playList[playListPos]

        setPlayer(song)
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
        binding.songAlbumIv.setImageResource(song.coverImg)
        binding.songStartTimeTv.text = String.format("%02d:%02d", playerSong.second / 60, playerSong.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", playerSong.playTime / 60, playerSong.playTime % 60)
        binding.songProgressSb.progress = (playerSong.second * 100000 / playerSong.playTime)

        if(song.isLike) binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        else binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)

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
        binding.songLikeIv.setOnClickListener {
            setLike(song.isLike)
            binding.songLikeIv.setImageResource(
                if(song.isLike) R.drawable.ic_my_like_on
                else R.drawable.ic_my_like_off
            )
        }
    }
    private fun setLike(isLike: Boolean){
        song.isLike = !isLike

        val songDB = SongDatabase.getInstance(this)!!
        songDB.songDao().updateIsLikeById(song.id, song.isLike)

        Log.d("Tester", songDB.songDao().getSongs().toString())
    }

    // 사용자가 포커스를 잃었을 때 음악 중지
    override fun onPause() {
        super.onPause()
        //setPlayerStatus(false)
        val sharedPreference = getSharedPreferences(CODE.music, MODE_PRIVATE)
        val edit = sharedPreference.edit()

        edit.putInt(CODE.playingSongID, song.id)
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
}