package com.example.landmarketmobileapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.models.NotificationType
import com.example.landmarketmobileapp.models.NotificationUI

@Composable
fun NotificationItem(
    notification: NotificationUI
) {
    val iconColor = when (notification.type) {
        NotificationType.SUCCESS -> Color(0xFF4CAF50)
        NotificationType.WARNING -> Color(0xFFFF9800)
        NotificationType.ERROR -> Color(0xFFF44336)
        NotificationType.NEW_MESSAGE -> Color(0xFF2196F3)
        NotificationType.NEW_AD -> Color(0xFF6AA26C)
        else -> Color(0xFF9E9E9E)
    }

    val icon = when (notification.type) {
        NotificationType.SUCCESS -> Icons.Default.CheckCircle
        NotificationType.WARNING -> Icons.Default.Warning
        NotificationType.ERROR -> Icons.Default.Warning
        NotificationType.NEW_MESSAGE -> Icons.Default.Email
        NotificationType.NEW_AD -> Icons.Default.Home
        else -> Icons.Default.Info
    }

    Card(
        modifier = Modifier.Companion.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (!notification.isRead) Color(0xFFE8F5E9) else Color.Companion.White
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Companion.Top
        ) {
            Icon(
                icon,
                contentDescription = notification.title,
                tint = iconColor,
                modifier = Modifier.Companion.size(24.dp)
            )

            Spacer(modifier = Modifier.Companion.width(16.dp))

            Column(
                modifier = Modifier.Companion.weight(1f)
            ) {
                Row(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Companion.Bold,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        maxLines = 1,
                        overflow = TextOverflow.Companion.Ellipsis
                    )

                    Text(
                        text = notification.time,
                        fontSize = 12.sp,
                        color = Color.Companion.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
                    )
                }

                Spacer(modifier = Modifier.Companion.height(4.dp))

                Text(
                    text = notification.message,
                    fontSize = 14.sp,
                    color = Color.Companion.DarkGray,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    maxLines = 2,
                    overflow = TextOverflow.Companion.Ellipsis
                )

                if (!notification.isRead) {
                    Spacer(modifier = Modifier.Companion.height(4.dp))

                    Box(
                        modifier = Modifier.Companion
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF6AA26C).copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Новое",
                            fontSize = 10.sp,
                            color = Color(0xFF6AA26C),
                            fontWeight = FontWeight.Companion.Bold,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                    }
                }
            }
        }
    }
}