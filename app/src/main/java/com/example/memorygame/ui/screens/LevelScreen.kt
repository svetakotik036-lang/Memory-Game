package com.example.memorygame.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memorygame.data.Difficulty
import com.example.memorygame.ui.GameViewModel
import com.example.memorygame.ui.theme.GlassBg
import com.example.memorygame.ui.theme.GlassStroke

@Composable
fun LevelScreen(vm: GameViewModel, onSelect: (Difficulty) -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(scrollState)
    ) {

        Spacer(Modifier.height(50.dp))

        Text(
            text = "Alege Nivelul",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Text(
            text = "Selectează categoria de vârstă potrivită",
            color = Color.White.copy(0.7f),
            fontSize = 16.sp
        )

        Spacer(Modifier.height(30.dp))

        Difficulty.entries.forEach { diff ->
            val best = vm.getBestTime(diff)
            val totalPairs = (diff.rows * diff.cols) / 2

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(GlassBg)
                    .border(1.dp, GlassStroke, RoundedCornerShape(24.dp))
                    .clickable { onSelect(diff) }
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(diff.color.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .border(1.dp, diff.color, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = diff.color
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        text = diff.label,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "${diff.ageRange} • $totalPairs perechi",
                        color = Color.White.copy(0.6f),
                        fontSize = 13.sp
                    )
                }
                if (best > 0) {
                    Column(horizontalAlignment = Alignment.End) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD600),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Best",
                            color = Color.White.copy(0.5f),
                            fontSize = 10.sp
                        )
                        Text(
                            text = "${best}s",
                            color = Color(0xFFFFD600),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(40.dp))
    }
}