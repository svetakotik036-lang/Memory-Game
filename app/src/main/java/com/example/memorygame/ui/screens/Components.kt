package com.example.memorygame.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memorygame.data.MemoryCard
import com.example.memorygame.ui.GameViewModel
import com.example.memorygame.ui.theme.ActionButtonPink
import com.example.memorygame.ui.theme.GlassBg
import com.example.memorygame.ui.theme.GlassBgFinal
import com.example.memorygame.ui.theme.GlassStroke
import com.example.memorygame.ui.theme.SuccessGreen
import java.util.Locale

@Composable
fun MemoryCardItem(card: MemoryCard, color: Color, onClick: () -> Unit) {
    val rotation by animateFloatAsState(
        targetValue = if (card.isFaceUp || card.isMatched) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "cardFlip"
    )

    Box(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .graphicsLayer { rotationY = rotation; cameraDistance = 12f * density }
            .clip(RoundedCornerShape(12.dp))
            .background(
                when {
                    card.isMatched -> SuccessGreen; card.isFaceUp -> color; else -> GlassBg
                }
            )
            .border(
                width = if (card.isMatched) 2.dp else 1.dp,
                color = if (card.isMatched) Color.White else GlassStroke,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (rotation > 90f) {
            Text(
                card.symbol,
                fontSize = 28.sp,
                modifier = Modifier.graphicsLayer { rotationY = 180f })
        }
    }
}

@Composable
fun WinDialog(vm: GameViewModel, onExit: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .clip(RoundedCornerShape(32.dp))
                .background(GlassBgFinal)
                .border(2.dp, GlassStroke, RoundedCornerShape(32.dp))
                .padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFFFFB300), CircleShape)
            ) {
                Icon(Icons.Default.Star, null, tint = Color.White, modifier = Modifier.size(60.dp))
            }
            Text(
                "Felicitări! 🎉",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))
            Row {
                repeat(3) { index ->
                    Icon(
                        Icons.Default.Star,
                        null,
                        tint = if (index < vm.stars) Color.Yellow else Color.White.copy(0.2f),
                        modifier = Modifier
                            .size(45.dp)
                            .padding(horizontal = 4.dp)
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
            StatCard(
                Icons.Default.DateRange,
                Color(0xFF03A9F4),
                "Timp",
                "${vm.time / 60}:${String.format(Locale.getDefault(), "%02d", vm.time % 60)}"
            )
            Spacer(Modifier.height(12.dp))
            StatCard(Icons.Default.CheckCircle, Color(0xFFE91E63), "Mutări", "${vm.moves}")
            Spacer(Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextButton(
                    onClick = onExit,
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(GlassBg)
                ) {
                    Text("Meniu", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { vm.initGame(vm.currentDiff) },
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ActionButtonPink)
                ) {
                    Text("Rejoacă", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StatCard(icon: ImageVector, color: Color, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(GlassBg)
            .border(1.dp, GlassStroke, RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(label, color = Color.White.copy(0.6f), fontSize = 12.sp)
            Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
    }
}