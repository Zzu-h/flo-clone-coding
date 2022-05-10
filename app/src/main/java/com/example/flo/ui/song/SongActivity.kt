package com.example.flo.ui.song

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.R
import com.example.flo.data.model.SongCode
import com.example.flo.data.model.Converter
import com.example.flo.data.model.SongDatabase
import com.example.flo.data.model.Timer
import com.example.flo.data.vo.Song
import com.example.flo.databinding.ActivitySongBinding
import com.example.flo.ui.animation.PlayerButtonAnimation

class SongActivity : AppCompatActivity() {

    //전역 변수
    lateinit var binding : ActivitySongBinding
    lateinit var song: Song
    lateinit var playList: MutableList<Song>
    private var playListSize = 0
    private var playListPos = 0

    private var timer: Timer? = null

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
        if(!intent.hasExtra(SongCode.playingSongID)) {
            finish()
            return
        }

        val sharedPreference = getSharedPreferences(SongCode.music, MODE_PRIVATE)
        val songId = sharedPreference.getInt(SongCode.playingSongID,0)

        val songDB = SongDatabase.getInstance(this)!!
        val dbPlayList = songDB.playListDao().getPlayList(SongCode.currentPlayList)
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
            PlayerButtonAnimation.bigAndSmall(binding.songNextIv)
            nextMusic()
        }
        binding.songPreviousIv.setOnClickListener {
            PlayerButtonAnimation.bigAndSmall(binding.songPreviousIv)
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
        val sharedPreference = getSharedPreferences(SongCode.music, MODE_PRIVATE)
        val edit = sharedPreference.edit()

        edit.putInt(SongCode.playingSongID, song.id)
        edit.apply()
    }
    override fun onDestroy() {
        super.onDestroy()
        timer?.interrupt()
        mediaPlayer?.release()
    }

    private fun setPlayerStatus (isPlaying : Boolean){
        song.isPlaying = isPlaying

        if(isPlaying){
            startTimer()
            PlayerButtonAnimation.pauseToPlay(binding.songPauseIv, binding.songMiniplayerIv)
            mediaPlayer?.apply {
                this.seekTo(song.mills.toInt())
                start()
            }
        } else {
            stopTimer()
            PlayerButtonAnimation.playToPause(binding.songMiniplayerIv, binding.songPauseIv)
            if(mediaPlayer?.isPlaying == true)
                mediaPlayer?.pause()
        }
    }
}