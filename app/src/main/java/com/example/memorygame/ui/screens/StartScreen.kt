package com.example.memorygame.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memorygame.audio.SoundManager
import com.example.memorygame.ui.theme.ActionButtonPink
import com.example.memorygame.ui.theme.GlassBg
import com.example.memorygame.ui.theme.GlassStroke
import kotlinx.coroutines.delay

@Composable
fun StartScreen(soundManager: SoundManager, onStart: () -> Unit) {
    var isMusicPlaying by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        soundManager.playBackgroundMusic()
        delay(500)
        isMusicPlaying = soundManager.isPlaying()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = {
                soundManager.toggleMusic()
                isMusicPlaying = soundManager.isPlaying()
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
                .size(50.dp)
                .background(GlassBg, CircleShape)
        ) {
            Icon(
                imageVector = if (isMusicPlaying) Icons.Default.MusicNote else Icons.Default.MusicOff,
                contentDescription = "Muzică",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = GlassBg,
                border = BorderStroke(1.dp, GlassStroke)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("🧠", fontSize = 60.sp)
                }
            }

            Spacer(Modifier.height(24.dp))

            Text("Memory Game", color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Bold)
            Text("Antrenează-ți mintea zilnic", color = Color.White.copy(0.7f), fontSize = 16.sp)

            Spacer(Modifier.height(60.dp))

            Button(
                onClick = onStart,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(60.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ActionButtonPink)
            ) {
                Text(
                    "Începe Jocul",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}