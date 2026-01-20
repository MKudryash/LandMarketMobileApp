package com.example.landmarketmobileapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.landmarketmobileapp.R

@Composable
fun MessageInputField(
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onAttachClick: () -> Unit,
    focusRequester: FocusRequester
) {
    Surface(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .background(Color.Companion.White),
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            // Кнопка прикрепления
            IconButton(
                onClick = onAttachClick,
                modifier = Modifier.Companion.size(40.dp)
            ) {
                Icon(
                    painterResource(R.drawable.attach),
                    contentDescription = "Прикрепить файл",
                    tint = Color(0xFF6AA26C)
                )
            }

            // Поле ввода
            OutlinedTextField(
                value = messageText,
                onValueChange = onMessageTextChange,
                modifier = Modifier.Companion
                    .weight(1f)
                    .focusRequester(focusRequester),
                placeholder = {
                    Text(
                        "Напишите сообщение...",
                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
                    )
                },
                shape = RoundedCornerShape(24.dp),
                singleLine = false,
                maxLines = 3,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Companion.Text,
                    imeAction = ImeAction.Companion.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = { onSendClick() }
                )
            )

            // Кнопка отправки
            IconButton(
                onClick = onSendClick,
                modifier = Modifier.Companion
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (messageText.isNotBlank()) Color(0xFF6AA26C) else Color.Companion.Gray.copy(
                            alpha = 0.3f
                        )
                    ),
                enabled = messageText.isNotBlank()
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Отправить",
                    tint = if (messageText.isNotBlank()) Color.Companion.White else Color.Companion.Gray
                )
            }
        }
    }
}