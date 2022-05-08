package com.example.flo.ui.main

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.flo.R
import com.example.flo.data.model.CODE
import com.example.flo.data.model.CODE.currentPlayList
import com.example.flo.data.model.CODE.playingSongID
import com.example.flo.data.model.Converter
import com.example.flo.data.model.SongDatabase
import com.example.flo.data.model.Timer
import com.example.flo.data.vo.Song
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.ui.main.home.HomeFragment
import com.example.flo.ui.main.locker.LockerFragment
import com.example.flo.ui.main.look.LookFragment
import com.example.flo.ui.main.search.SearchFragment
import com.example.flo.ui.song.SongActivity
import com.example.flo.data.vo.Album
import com.example.flo.data.vo.PlayList
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var song: Song
    lateinit var playList: MutableList<Song>
    private var playListSize = 0
    private var playListPos = 0

    private var timer: Timer? = null
    private val aniDuration = 300L

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_FLO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        playList = mutableListOf<Song>()

        setContentView(binding.root)
        inputDummies()

        initClickListener()

        initBottomNavigation()
        binding.mainPlayerCl.isClickable = true
        binding.mainPlayerCl.setOnClickListener { startActivity(Intent(this, SongActivity::class.java)) }
        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            val editor = getSharedPreferences(CODE.music, MODE_PRIVATE).edit()
            editor.putInt(CODE.playingSongID, song.id)
            editor.apply()

            intent.putExtra(CODE.playingSongID, song.id)

            startActivity(intent)
        }
    }
    override fun onStart() {
        super.onStart()

        val sharedPreference = getSharedPreferences(CODE.music, MODE_PRIVATE)
        val songId = sharedPreference.getInt(playingSongID,0)

        val songDB = SongDatabase.getInstance(this)!!
        val dbPlayList = songDB.playListDao().getPlayList(currentPlayList)
        val songIdList = Converter.stringToList(dbPlayList)

        songIdList.forEach { playList.add(songDB.songDao().getSong(it)) }

        if(songId != 0) playList.forEachIndexed { index, c -> if(songId == c.id) playListPos = index }
        else playListPos = 0

        playListSize = songIdList.size
        song = playList[playListPos]

        setMiniPlayer(song)
    }

    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
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

    fun playAlbum(album: Album){
        val idList = Converter.stringToList(album.songIdList)
        this.playList = mutableListOf()

        playListPos = 0
        playListSize = idList.size

        val songDB = SongDatabase.getInstance(this)!!
        songDB.playListDao().deletePlayList(currentPlayList)
        songDB.playListDao().insert(PlayList(Converter.listToString(idList), currentPlayList))

        idList.forEach { playList.add(songDB.songDao().getSong(it)) }

        song = playList[playListPos]
        song.isPlaying = true

        setMiniPlayer(song)
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
    private fun initClickListener(){
        binding.mainMiniplayerBtn.setOnClickListener { setPlayerStatus(true) }
        binding.mainPauseBtn.setOnClickListener { setPlayerStatus(false) }
        binding.songNextIv.setOnClickListener {
            aniBigSmall(binding.songNextIv)
            nextMusic()
        }
        binding.songPreviousIv.setOnClickListener {
            aniBigSmall(binding.songPreviousIv)
            prevMusic()
        }
    }

    private fun inputDummies(){
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()
        val albums = songDB.albumDao().getAlbums()
        val dbPlayList = songDB.playListDao().getPlayList(CODE.currentPlayList)

        if(songs.isEmpty()){
            songDB.songDao().insert(Song("라일락", "아이유 (IU)", 0, 60, false, 0f, "music_lilac", isTitle = true))
            songDB.songDao().insert(Song("Flu", "아이유 (IU)", 0, 60, false, 0f, "music_flu", isTitle = false))
            songDB.songDao().insert(Song("Coin", "아이유 (IU)", 0, 60, false, 0f, "music_coin", isTitle = true))
            songDB.songDao().insert(Song("봄 안녕 봄", "아이유 (IU)", 0, 60, false, 0f, "music_hispringbye", isTitle = false))

            songDB.songDao().insert(Song("BBoom BBoom", "모모랜드 (MOMOLAND)", 0, 60, false, 0f, "music_bboom", isTitle = true, coverImg = R.drawable.img_album_exp5))
            songDB.songDao().insert(Song("작은 것들을 위한 시", "BTS", 0, 60, false, 0f, "music_boy", isTitle = true, coverImg = R.drawable.img_album_exp4))
            songDB.songDao().insert(Song("Butter", "BTS", 0, 60, false, 0f, "music_butter", isTitle = true, coverImg = R.drawable.img_album_exp))
        }
        if(albums.isEmpty()){
            val allSongs = songDB.songDao().getSongs()
            val IUlist = mutableListOf<Song>()
            val BTSlist = mutableListOf<Song>()

            allSongs.forEach {
                val song = it
                if(it.singer.contains("아이유")) IUlist.add(it)
                else if(it.singer.contains("BTS")) BTSlist.add(it)
                else songDB.albumDao().insert(Album(songIdList = Converter.listToString(List(1) { song.id }), title = "BBoom BBoom", singer = "모모랜드 (MOMOLAND)", coverImg = R.drawable.img_album_exp5))
            }
            songDB.albumDao().insert(Album(songIdList = Converter.listToString(List(IUlist.size) { i -> IUlist[i].id }), title = "Lilac", singer = "아이유 (IU)"))
            BTSlist.forEach {
                val song = it
                if(it.title.contains("Butter")) songDB.albumDao().insert(Album(songIdList = Converter.listToString(List(1) { song.id }), title = "Butter", singer = "BTS", coverImg = R.drawable.img_album_exp))
                else songDB.albumDao().insert(Album(songIdList = Converter.listToString(List(1) { song.id }), title = "MAP OF THE SOUL : PERSONA", singer = "BTS", coverImg = R.drawable.img_album_exp4))
            }

        }
        if(dbPlayList == null || dbPlayList.isEmpty()){
            val allSongs = songDB.songDao().getSongs()
            val list = List(allSongs.size){ index -> allSongs[index].id }

            songDB.playListDao().insert(PlayList(Converter.listToString(list),CODE.currentPlayList))
        }
    }

    private fun setMiniPlayer(playerSong: Song){
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

        binding.mainMiniplayerTitleTv.text = playerSong.title
        binding.mainMiniplayerSingerTv.text = playerSong.singer
        binding.songProgressSb.progress = (playerSong.second * 100000 / playerSong.playTime)

        setPlayerStatus(playerSong.isPlaying)
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
    private fun prevMusic(){
        playListPos = ((playListPos - 1) + playListSize).mod(playListSize)
        song = playList[playListPos]

        setMiniPlayer(song)
    }
    private fun nextMusic(){
        playListPos = (playListPos + 1).mod(playListSize)
        song = playList[playListPos]

        setMiniPlayer(song)
    }

    private fun startTimer(){
        timer = Timer(this, song, song.playTime, song.isPlaying, song.second, song.mills)
            .apply {
                nextMusic = this@MainActivity::nextMusic
                setPlayerStatus = this@MainActivity::setPlayerStatus
                updateProgressSb = this@MainActivity::updateProgressSb
            }
        timer?.start()
    }
    private fun stopTimer() {
        if(timer == null) return
        timer?.interrupt()
    }
    private fun updateProgressSb(time: Int) { binding.songProgressSb.progress = time }
}