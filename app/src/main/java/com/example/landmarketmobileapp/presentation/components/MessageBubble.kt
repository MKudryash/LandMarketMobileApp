package com.example.landmarketmobileapp.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.models.state.MessageUI

@Composable
fun MessageBubble(message: MessageUI) {
    val bubbleColor = if (message.isSentByMe) {
        Color(0xFF6AA26C)
    } else {
        Color.Companion.White
    }

    val textColor = if (message.isSentByMe) {
        Color.Companion.White
    } else {
        Color.Companion.Black
    }

    Column(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalAlignment = if (message.isSentByMe) Alignment.Companion.End else Alignment.Companion.Start
    ) {
        Surface(
            modifier = Modifier.Companion
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isSentByMe) 16.dp else 4.dp,
                        bottomEnd = if (message.isSentByMe) 4.dp else 16.dp
                    )
                ),
            color = bubbleColor,
            tonalElevation = 1.dp,
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier.Companion.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = message.text,
                    fontSize = 15.sp,
                    color = textColor,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular))
                )
            }
        }

        Row(
            modifier = Modifier.Companion.padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            Text(
                text = message.time,
                fontSize = 11.sp,
                color = Color.Companion.Gray,
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )

            if (message.isSentByMe) {
                Spacer(modifier = Modifier.Companion.width(4.dp))

                Icon(
                    if (message.isRead) Icons.Default.Done else Icons.Default.Done,
                    contentDescription = "Статус",
                    tint = if (message.isRead) Color(0xFF6AA26C) else Color.Companion.Gray,
                    modifier = Modifier.Companion.size(12.dp)
                )
            }
        }
    }
}