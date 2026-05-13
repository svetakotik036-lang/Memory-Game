package com.example.memorygame.data

import androidx.compose.ui.graphics.Color

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