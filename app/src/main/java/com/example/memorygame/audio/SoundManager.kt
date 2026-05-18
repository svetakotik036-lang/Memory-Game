package com.example.memorygame.audio

import android.content.Context
import android.media.MediaPlayer
import com.example.memorygame.R

class SoundManager(val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun playBackgroundMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.bg_music)
            mediaPlayer?.isLooping = true
            mediaPlayer?.setVolume(0.4f, 0.4f)
        }
        mediaPlayer?.start()
    }

    fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false

    fun toggleMusic() {
        if (isPlaying()) {
            mediaPlayer?.pause()

        } else {
            mediaPlayer?.start()
        }
    }

    fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}