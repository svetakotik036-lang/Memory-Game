package com.example.memorygame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.memorygame.audio.SoundManager
import com.example.memorygame.ui.screens.MemoryApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var soundManager: SoundManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        soundManager = SoundManager(this)

        lifecycleScope.launch {
            delay(500)
            soundManager.playBackgroundMusic()
        }

        setContent {
            MemoryApp(soundManager = soundManager)
        }
    }

    override fun onResume() {
        super.onResume()
        if (::soundManager.isInitialized) {
            soundManager.playBackgroundMusic()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::soundManager.isInitialized && soundManager.isPlaying()) {
            soundManager.toggleMusic()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::soundManager.isInitialized) {
            soundManager.stopMusic()
        }
    }
}