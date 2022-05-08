package com.example.flo.ui.animation

import android.view.View
import android.widget.ImageView

object PlayerButtonAnimation {
    private const val aniDuration = 300L

    fun bigAndSmall(view: ImageView){
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
    fun pauseToPlay(pause: ImageView, play: ImageView){
        pause.animate().apply {
            rotationY(-90f)
        }.start()
        play.animate().apply {
            duration = aniDuration
            rotationYBy(90f)
        }.withEndAction {
            play.visibility = View.GONE
            pause.visibility = View.VISIBLE
            pause.animate().apply {
                duration = aniDuration
                rotationYBy(90f)
            }.start()
        }
        play.animate().apply {
            rotationYBy(-90f)
        }.start()
    }
    fun playToPause(play: ImageView, pause: ImageView){
        play.animate().apply {
            rotationY(-90f)
        }.start()
        pause.animate().apply {
            duration = aniDuration
            rotationYBy(90f)
        }.withEndAction {
            play.visibility = View.VISIBLE
            pause.visibility = View.GONE
            play.animate().apply {
                duration = aniDuration
                rotationYBy(90f)
            }.start()
        }
        pause.animate().apply {
            rotationYBy(-90f)
        }.start()
    }
}