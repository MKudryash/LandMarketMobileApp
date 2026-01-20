package com.example.landmarketmobileapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.models.ChatUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    chat: ChatUI,
    onBackClick: () -> Unit,
    onCallClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.Companion.CenterVertically,
                modifier = Modifier.Companion.clickable(onClick = onInfoClick)
            ) {
                // Аватар
                Box(contentAlignment = Alignment.Companion.BottomEnd) {
                    Box(
                        modifier = Modifier.Companion
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6AA26C))
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = chat.participantName,
                            tint = Color.Companion.White,
                            modifier = Modifier.Companion
                                .size(20.dp)
                                .align(Alignment.Companion.Center)
                        )
                    }

                }

                Spacer(modifier = Modifier.Companion.width(12.dp))

                Column {
                    Text(
                        text = chat.participantName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Companion.Bold,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF6AA26C),
            titleContentColor = Color.Companion.White,
            navigationIconContentColor = Color.Companion.White,
            actionIconContentColor = Color.Companion.White
        ),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
            }
        },
        actions = {
            IconButton(onClick = onCallClick) {
                Icon(Icons.Default.Call, contentDescription = "Позвонить")
            }

            IconButton(onClick = { /* Меню чата */ }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Еще")
            }
        }
    )
}