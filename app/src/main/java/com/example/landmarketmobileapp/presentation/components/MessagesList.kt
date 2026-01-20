package com.example.landmarketmobileapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
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
import com.example.landmarketmobileapp.models.state.MessagesState

@Composable
fun MessagesList(
    messagesState: MessagesState?,
    listState: LazyListState,
    modifier: Modifier = Modifier.Companion
) {
    when {
        messagesState == null || messagesState.isLoading -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Companion.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF6AA26C))
            }
        }

        messagesState.error != null -> {
            Box(
                modifier = modifier,
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
                        text = messagesState.error,
                        fontSize = 16.sp,
                        color = Color.Companion.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_medium))
                    )
                }
            }
        }

        messagesState.messages.isEmpty() -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Companion.Center
            ) {
                Text(
                    text = "Нет сообщений",
                    fontSize = 16.sp,
                    color = Color.Companion.Gray,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular))
                )
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier,
                state = listState,
                reverseLayout = false,
                verticalArrangement = Arrangement.Bottom,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(messagesState.messages) { message ->
                    MessageBubble(message = message)
                }
            }
        }
    }
}