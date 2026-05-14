package com.example.memorygame.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memorygame.ui.theme.ActionButtonPink
import com.example.memorygame.ui.theme.GlassBg
import com.example.memorygame.ui.theme.GlassStroke

@Composable
//Un start al programului
fun StartScreen(onStart: () -> Unit) {
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
            Box(contentAlignment = Alignment.Center) { Text("🧠", fontSize = 60.sp) }
        }
        Spacer(Modifier.height(24.dp))
        Text("Memory Game", color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Bold)
        Text("Antrenează-ți mintea zilnic", color = Color.White.copy(0.7f))
        Spacer(Modifier.height(60.dp))
        Button(
            onClick = onStart,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(60.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ActionButtonPink)
        ) {
            Text("Începe Jocul", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}