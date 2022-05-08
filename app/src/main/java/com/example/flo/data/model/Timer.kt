package com.example.flo.data.model

import android.app.Activity
import com.example.flo.data.vo.Song
import java.lang.Exception

class Timer(val activity: Activity, val song: Song, private val playTime: Int, var isPlaying: Boolean = true, private var second: Int = 0, private var mills: Float = 0f): Thread() {
    private var runningFlag = true

    var nextMusic: () -> Unit =  {}
    var setPlayerStatus: (Boolean) -> Unit =  {}
    var updateProgressSb: (Int) -> Unit =  {}

    override fun run() {
        super.run()
        try {
            runningFlag = true
            while (runningFlag){
                if(second >= playTime) {
                    song.mills = 0f
                    song.second = 0
                    nextMusic()
                    setPlayerStatus(false)
                    break
                }
                if(isPlaying){
                    sleep(50)
                    mills += 50
                    song.mills = mills
                    activity.runOnUiThread { updateProgressSb(((mills / playTime)*100).toInt()) }
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