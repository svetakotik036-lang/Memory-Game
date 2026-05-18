package com.example.memorygame.ui

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.memorygame.data.Difficulty
import com.example.memorygame.data.MemoryCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs: SharedPreferences =
        application.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
    private val symbols = listOf(
        "⚡", "🔥", "💎", "🌟", "🍎", "🌈", "🎈", "🍀", "🐱", "🚀", "🎸", "🍕",
        "⚽", "🍄", "🍦", "👑", "🎨", "🌍", "✈️", "🏀", "🍔", "🚲", "🎮", "🦄"
    )

    var currentDiff by mutableStateOf(Difficulty.EASY)
    private val _cards = MutableStateFlow<List<MemoryCard>>(emptyList())
    val cards = _cards.asStateFlow()

    var moves by mutableIntStateOf(0)
    var matches by mutableIntStateOf(0)
    var time by mutableIntStateOf(0)
    var isPlaying by mutableStateOf(false)
    var isPreviewing by mutableStateOf(false)
    var showWinDialog by mutableStateOf(false)
    var busy by mutableStateOf(false)
    var stars by mutableIntStateOf(0)
    private var firstIndex: Int? = null

    fun getBestTime(diff: Difficulty): Int = prefs.getInt("best_${diff.name}", 0)

    fun initGame(diff: Difficulty) {
        currentDiff = diff
        val totalPairs = (diff.rows * diff.cols) / 2
        val gameSymbols = (symbols.take(totalPairs) + symbols.take(totalPairs)).shuffled()

        _cards.value = gameSymbols.mapIndexed { index, s ->
            MemoryCard(id = index, symbol = s, isFaceUp = true)
        }

        moves = 0
        matches = 0
        time = 0
        firstIndex = null
        showWinDialog = false
        isPreviewing = true
        busy = true
        isPlaying = false

        viewModelScope.launch {
            delay(3000)
            _cards.value = _cards.value.map { it.copy(isFaceUp = false) }
            isPreviewing = false
            busy = false
            isPlaying = true
            startTimer()
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (isPlaying) {
                delay(1000)
                if (isPlaying) time++
            }
        }
    }

    fun onCardClick(index: Int) {
        if (busy || isPreviewing || !isPlaying) return
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
                        calculateResult()
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

    private fun calculateResult() {
        val minMoves = (currentDiff.rows * currentDiff.cols) / 2
        stars = when {
            moves <= minMoves + 3 -> 3
            moves <= minMoves + 8 -> 2
            else -> 1
        }
        val currentBest = getBestTime(currentDiff)
        if (currentBest == 0 || time < currentBest) {
            prefs.edit { putInt("best_${currentDiff.name}", time) }
        }
    }
}