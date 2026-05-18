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
            .padding(2.dp)
            .aspectRatio(1f)
            .graphicsLayer { rotationY = rotation; cameraDistance = 12f * density }
            .clip(RoundedCornerShape(12.dp))
            .background(
                when {
                    card.isMatched -> SuccessGreen
                    card.isFaceUp -> color
                    else -> color
                }
            )
            .border(
                width = if (card.isMatched) 2.dp else 1.5.dp,
                color = if (card.isMatched) Color.White else Color.White.copy(0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (rotation > 90f) {
            Text(
                text = card.symbol,
                fontSize = 24.sp,
                modifier = Modifier.graphicsLayer { rotationY = 180f }
            )
        } else {
            Text(
                text = "?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFFFB300), CircleShape)
            ) {
                Icon(Icons.Default.Star, null, tint = Color.White, modifier = Modifier.size(50.dp))
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "Felicitări! 🎉",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            Row {
                repeat(3) { index ->
                    Icon(
                        Icons.Default.Star, null,
                        tint = if (index < vm.stars) Color.Yellow else Color.White.copy(0.2f),
                        modifier = Modifier
                            .size(40.dp)
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
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(
                    onClick = onExit,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(GlassBg)
                ) {
                    Text("Meniu", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { vm.initGame(vm.currentDiff) },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ActionButtonPink)
                ) {
                    Text("Rejoacă", fontWeight = FontWeight.Bold, color = Color.White)
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
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, color = Color.White.copy(0.6f), fontSize = 11.sp)
            Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}