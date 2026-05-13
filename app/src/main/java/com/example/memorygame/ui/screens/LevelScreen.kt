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
import androidx.compose.foundation.shape.RoundedCornerShape
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Spacer(Modifier.height(40.dp))

        Text(
            "Alege Nivelul",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Text("Selectează dificultatea dorită", color = Color.White.copy(0.7f))

        Spacer(Modifier.height(30.dp))

        // Iterăm prin valorile Enum-ului Difficulty
        Difficulty.entries.forEach { diff ->
            val best = vm.getBestTime(diff)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(GlassBg)
                    .border(1.dp, GlassStroke, RoundedCornerShape(24.dp))
                    .clickable { onSelect(diff) }
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(diff.color, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PlayArrow, null, tint = Color.White)
                }

                Spacer(Modifier.width(16.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        diff.label,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(diff.desc, color = Color.White.copy(0.6f), fontSize = 14.sp)
                }

                if (best > 0) {
                    Column(horizontalAlignment = Alignment.End) {
                        Icon(
                            Icons.Default.Star,
                            null,
                            tint = Color.Yellow,
                            modifier = Modifier.size(18.dp)
                        )
                        Text("Best", color = Color.White.copy(0.5f), fontSize = 10.sp)
                        Text(
                            "${best}s",
                            color = Color.Yellow,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}