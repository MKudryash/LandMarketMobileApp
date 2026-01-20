package com.example.landmarketmobileapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R

@Composable
fun EmptyChatsState() {
    Box(
        modifier = Modifier.Companion
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Companion.Center
    ) {
        Column(
            horizontalAlignment = Alignment.Companion.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painterResource(R.drawable.chat),
                contentDescription = "Нет чатов",
                tint = Color.Companion.Gray,
                modifier = Modifier.Companion.size(80.dp)
            )

            Spacer(modifier = Modifier.Companion.height(16.dp))

            Text(
                text = "Нет сообщений",
                fontSize = 20.sp,
                fontWeight = FontWeight.Companion.Bold,
                color = Color.Companion.Gray,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )

            Text(
                text = "Здесь будут отображаться ваши чаты",
                fontSize = 14.sp,
                color = Color.Companion.Gray,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                modifier = Modifier.Companion.padding(top = 8.dp)
            )
        }
    }
}