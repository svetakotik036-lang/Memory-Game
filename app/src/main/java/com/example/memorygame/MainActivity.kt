package com.example.memorygame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ================= CULORI & TEMA =================

val BgStart = Color(0xFF3E2B8D)
val BgEnd = Color(0xFFC01555)
val ActionButtonPink = Color(0xFFE91E63)

val GlassBg = Color(0x26FFFFFF)
val GlassBgFinal = Color(0x33FFFFFF)
val GlassStroke = Color(0x4DFFFFFF)

// ================= DIFICULTATE =================

enum class Difficulty(
    val rows: Int,
    val cols: Int,
    val label: String,
    val desc: String,
    val color: Color
) {
    EASY(4, 4, "Ușor", "4x4 Grid • 8 perechi", Color(0xFF00C853)),
    MEDIUM(6, 4, "Mediu", "6x4 Grid • 12 perechi", Color(0xFFFF6D00)),
    HARD(8, 4, "Dificil", "8x4 Grid • 16 perechi", Color(0xFFD50000))
}

data class MemoryCard(
    val id: Int,
    val symbol: String,
    val isFaceUp: Boolean = false,
    val isMatched: Boolean = false
)

// ================= VIEWMODEL =================

class GameViewModel : ViewModel() {
    private val symbols = listOf("⚡", "🔥", "💎", "🌟", "🍎", "🌈", "🎈", "🍀", "🐱", "🚀", "🎸", "🍕", "⚽", "🍄", "🍦", "👑")

    var currentDiff by mutableStateOf(Difficulty.EASY)
    private val _cards = MutableStateFlow<List<MemoryCard>>(emptyList())
    val cards = _cards.asStateFlow()

    var moves by mutableStateOf(0)
    var matches by mutableStateOf(0)
    var time by mutableStateOf(0)
    var isPlaying by mutableStateOf(false)
    var showWinDialog by mutableStateOf(false)
    var busy by mutableStateOf(false)
    private var firstIndex: Int? = null

    fun initGame(diff: Difficulty) {
        currentDiff = diff
        val totalPairs = (diff.rows * diff.cols) / 2
        val gameSymbols = (symbols.take(totalPairs) + symbols.take(totalPairs)).shuffled()

        _cards.value = gameSymbols.mapIndexed { index, s -> MemoryCard(id = index, symbol = s) }
        moves = 0
        matches = 0
        time = 0
        firstIndex = null
        busy = false
        showWinDialog = false
        isPlaying = true
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (isPlaying) {
                delay(1000)
                time++
            }
        }
    }

    fun onCardClick(index: Int) {
        if (busy) return
        val currentCards = _cards.value.toMutableList()
        if (currentCards[index].isFaceUp || currentCards[index].isMatched) return

        currentCards[index] = currentCards[index].copy(isFaceUp = true)
        _cards.value = currentCards

        if (firstIndex == null) {
            firstIndex = index
        } else {
            val first = firstIndex!!
            moves++
            busy = true
            viewModelScope.launch {
                delay(600)
                val updatedCards = _cards.value.toMutableList()
                if (updatedCards[first].symbol == updatedCards[index].symbol) {
                    updatedCards[first] = updatedCards[first].copy(isMatched = true)
                    updatedCards[index] = updatedCards[index].copy(isMatched = true)
                    matches++
                    if (matches == (currentDiff.rows * currentDiff.cols) / 2) {
                        isPlaying = false
                        showWinDialog = true
                    }
                } else {
                    updatedCards[first] = updatedCards[first].copy(isFaceUp = false)
                    updatedCards[index] = updatedCards[index].copy(isFaceUp = false)
                }
                _cards.value = updatedCards
                firstIndex = null
                busy = false
            }
        }
    }
}

// ================= UI COMPONENTS =================

@Composable
fun MemoryApp() {
    val vm: GameViewModel = viewModel()
    var screen by remember { mutableStateOf("start") }
    val backgroundBrush = Brush.verticalGradient(listOf(BgStart, BgEnd))

    Box(modifier = Modifier.fillMaxSize().background(backgroundBrush)) {
        when (screen) {
            "start" -> StartScreen { screen = "levels" }
            "levels" -> LevelScreen { diff -> vm.initGame(diff); screen = "game" }
            "game" -> GameScreen(vm) { screen = "levels" }
        }
    }
}

@Composable
fun StartScreen(onStart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(modifier = Modifier.size(120.dp), shape = CircleShape, color = GlassBg, border = BorderStroke(1.dp, GlassStroke)) {
            Box(contentAlignment = Alignment.Center) { Text("🧠", fontSize = 60.sp) }
        }
        Spacer(Modifier.height(24.dp))
        Text("Memory Game", color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Bold)
        Text("Antrenează-ți mintea zilnic", color = Color.White.copy(0.7f))
        Spacer(Modifier.height(60.dp))
        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth(0.7f).height(60.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ActionButtonPink)
        ) {
            Text("Începe Jocul", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun LevelScreen(onSelect: (Difficulty) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Spacer(Modifier.height(40.dp))
        Text("Alege Nivelul", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
        Text("Selectează dificultatea dorită", color = Color.White.copy(0.7f))
        Spacer(Modifier.height(30.dp))
        Difficulty.entries.forEach { diff ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clip(RoundedCornerShape(24.dp))
                    .background(GlassBg).border(1.dp, GlassStroke, RoundedCornerShape(24.dp))
                    .clickable { onSelect(diff) }.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(50.dp).background(diff.color, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.PlayArrow, null, tint = Color.White)
                }
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    Text(diff.label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(diff.desc, color = Color.White.copy(0.6f), fontSize = 14.sp)
                }
                Icon(Icons.Default.Star, null, tint = Color.Yellow)
            }
        }
    }
}

@Composable
fun GameScreen(vm: GameViewModel, onExit: () -> Unit) {
    val cards by vm.cards.collectAsState()
    val totalPairs = (vm.currentDiff.rows * vm.currentDiff.cols) / 2
    val progress = if (totalPairs == 0) 0f else vm.matches.toFloat() / totalPairs.toFloat()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(top = 20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onExit, modifier = Modifier.background(GlassBg, CircleShape)) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) }
            Text("⏱ ${vm.time / 60}:${String.format("%02d", vm.time % 60)}", color = Color.White, modifier = Modifier.background(GlassBg, RoundedCornerShape(12.dp)).padding(8.dp))
            Text("🎯 ${vm.moves} Mutări", color = Color.White, modifier = Modifier.background(GlassBg, RoundedCornerShape(12.dp)).padding(8.dp))
        }
        Spacer(Modifier.height(20.dp))
        LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape), color = Color.Cyan, trackColor = GlassBg)
        Spacer(Modifier.height(20.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(vm.currentDiff.cols), modifier = Modifier.weight(1f)) {
            items(cards, key = { it.id }) { card ->
                MemoryCardItem(card, vm.currentDiff.color) { vm.onCardClick(card.id) }
            }
        }
    }

    if (vm.showWinDialog) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(0.7f)), contentAlignment = Alignment.Center) {
            Column(modifier = Modifier.fillMaxWidth(0.85f).clip(RoundedCornerShape(32.dp)).background(GlassBgFinal)
                .border(2.dp, GlassStroke, RoundedCornerShape(32.dp)).padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🏆", fontSize = 80.sp)
                Text("Felicitări! 🎉", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                Row {
                    Icon(Icons.Default.Star, null, tint = Color.Yellow, modifier = Modifier.size(36.dp))
                    Icon(Icons.Default.Star, null, tint = Color.White.copy(0.2f), modifier = Modifier.size(36.dp))
                    Icon(Icons.Default.Star, null, tint = Color.White.copy(0.2f), modifier = Modifier.size(36.dp))
                }
                Spacer(Modifier.height(24.dp))
                // Înlocuit WatchLater cu Settings (Iconiță de bază, funcționează sigur)
                StatCard(Icons.Default.Settings, Color(0xFF03A9F4), "Timp", "${vm.time / 60}:${String.format("%02d", vm.time % 60)}")
                Spacer(Modifier.height(12.dp))
                // Înlocuit TouchApp cu CheckCircle (Iconiță de bază, funcționează sigur)
                StatCard(Icons.Default.CheckCircle, Color(0xFFE91E63), "Mutări", "${vm.moves}")
                Spacer(Modifier.height(32.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    TextButton(onClick = onExit, modifier = Modifier.weight(1f).height(55.dp).clip(RoundedCornerShape(16.dp)).background(GlassBg)) {
                        Text("Meniu", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Button(onClick = { vm.initGame(vm.currentDiff) }, modifier = Modifier.weight(1f).height(55.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = ActionButtonPink)) {
                        Text("Rejoacă", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun MemoryCardItem(card: MemoryCard, color: Color, onClick: () -> Unit) {
    Box(modifier = Modifier.padding(4.dp).aspectRatio(1f).clip(RoundedCornerShape(12.dp))
        .background(if (card.isFaceUp || card.isMatched) color else GlassBg)
        .border(1.dp, GlassStroke, RoundedCornerShape(12.dp)).clickable { onClick() }, contentAlignment = Alignment.Center) {
        if (card.isFaceUp || card.isMatched) Text(card.symbol, fontSize = 28.sp)
    }
}

@Composable
fun StatCard(icon: ImageVector, color: Color, label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(GlassBg).border(1.dp, GlassStroke, RoundedCornerShape(16.dp)).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(color), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(label, color = Color.White.copy(0.6f), fontSize = 12.sp)
            Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MemoryApp() }
    }
}