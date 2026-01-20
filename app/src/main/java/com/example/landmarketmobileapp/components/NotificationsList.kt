package com.example.landmarketmobileapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.viewModels.NotificationsState

@Composable
fun NotificationsList(
    notificationState: NotificationsState,
    onRefresh: () -> Unit
) {
    when {
        notificationState.isLoading -> {
            Box(
                modifier = Modifier.Companion.fillMaxSize(),
                contentAlignment = Alignment.Companion.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF6AA26C))
            }
        }

        notificationState.error != null -> {
            Box(
                modifier = Modifier.Companion.fillMaxSize(),
                contentAlignment = Alignment.Companion.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.Companion.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "Ошибка",
                        tint = Color.Companion.Red,
                        modifier = Modifier.Companion.size(64.dp)
                    )

                    Spacer(modifier = Modifier.Companion.height(16.dp))

                    Text(
                        text = notificationState.error,
                        fontSize = 16.sp,
                        color = Color.Companion.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_medium))
                    )

                    Spacer(modifier = Modifier.Companion.height(16.dp))

                    Button(
                        onClick = onRefresh,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6AA26C)
                        )
                    ) {
                        Text("Повторить")
                    }
                }
            }
        }

        notificationState.notifications.isEmpty() -> {
            EmptyNotificationsState()
        }

        else -> {
            LazyColumn(
                modifier = Modifier.Companion.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                items(notificationState.notifications) { notification ->
                    NotificationItem(notification = notification)
                }
            }
        }
    }
}