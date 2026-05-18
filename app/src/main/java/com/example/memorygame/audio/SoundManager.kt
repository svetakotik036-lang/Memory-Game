package com.example.memorygame.audio

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.example.memorygame.R

class SoundManager(context: Context) {
    private val appContext = context.applicationContext
    private var mediaPlayer: MediaPlayer? = null

    fun playBackgroundMusic() {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(appContext, R.raw.bg_music)

                if (mediaPlayer == null) {
                    Log.e(
                        "SoundManager",
                        "EROARE: Fișierul audio nu a putut fi încărcat din res/raw"
                    )
                    return
                }

                mediaPlayer?.isLooping = true
                mediaPlayer?.setVolume(0.8f, 0.8f)

                mediaPlayer?.setOnErrorListener { _, what, extra ->
                    Log.e("SoundManager", "MediaPlayer Error: what=$what, extra=$extra")
                    stopMusic()
                    true
                }
            }

            if (mediaPlayer?.isPlaying == false) {
                mediaPlayer?.start()
                Log.d("SoundManager", "Muzica pornită cu succes")
            }
        } catch (e: Exception) {
            Log.e("SoundManager", "Excepție la pornirea muzicii: ${e.message}")
        }
    }

    fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false

    fun toggleMusic() {
        if (isPlaying()) {
            mediaPlayer?.pause()
        } else {
            playBackgroundMusic()
        }
    }

    fun stopMusic() {
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        mediaPlayer = null
    }
}