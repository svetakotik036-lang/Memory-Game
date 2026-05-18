package com.example.memorygame.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.memorygame.audio.SoundManager
import com.example.memorygame.ui.GameViewModel
import com.example.memorygame.ui.theme.BgEnd
import com.example.memorygame.ui.theme.BgStart
import com.example.memorygame.ui.theme.GlassBg
import java.util.Locale

@Composable
fun MemoryApp(soundManager: SoundManager) {
    val vm: GameViewModel = viewModel()
    var screen by remember { mutableStateOf("start") }
    val backgroundBrush = Brush.verticalGradient(listOf(BgStart, BgEnd))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        when (screen) {
            "start" -> StartScreen(soundManager) { screen = "levels" }
            "levels" -> LevelScreen(vm) { diff -> vm.initGame(diff); screen = "game" }
            "game" -> GameScreen(vm, soundManager) { screen = "levels" }
        }
    }
}

@Suppress("SpellCheckingInspection")
@Composable
fun GameScreen(vm: GameViewModel, soundManager: SoundManager, onExit: () -> Unit) {
    val cards by vm.cards.collectAsState()
    val totalPairs = (vm.currentDiff.rows * vm.currentDiff.cols) / 2
    val progressValue = if (totalPairs == 0) 0f else vm.matches.toFloat() / totalPairs.toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onExit, modifier = Modifier.background(GlassBg, CircleShape)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }
            var isPlaying by remember { mutableStateOf(soundManager.isPlaying()) }
            IconButton(
                onClick = {
                    soundManager.toggleMusic()
                    isPlaying = soundManager.isPlaying()
                },
                modifier = Modifier.background(GlassBg, CircleShape)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.MusicNote else Icons.Default.MusicOff,
                    contentDescription = "Muzică",
                    tint = Color.White
                )
            }

            Text(
                "⏱ ${vm.time / 60}:${String.format(Locale.getDefault(), "%02d", vm.time % 60)}",
                color = Color.White,
                modifier = Modifier
                    .background(GlassBg, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
            Text(
                "🎯 ${vm.moves}",
                color = Color.White,
                modifier = Modifier
                    .background(GlassBg, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        if (vm.isPreviewing) {
            Text(
                "Memorizează cărțile! 🧠",
                color = Color.Yellow,
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
        } else {
            LinearProgressIndicator(
                progress = progressValue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(CircleShape),
                color = Color.Cyan,
                trackColor = GlassBg
            )
        }

        Spacer(Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(vm.currentDiff.cols),
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cards, key = { it.id }) { card ->
                MemoryCardItem(card, vm.currentDiff.color) {
                    vm.onCardClick(card.id)
                }
            }
        }
    }

    if (vm.showWinDialog) {
        WinDialog(vm, onExit)
    }
}