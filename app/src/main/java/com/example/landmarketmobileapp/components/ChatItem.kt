package com.example.landmarketmobileapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.models.ChatUI
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ChatItem(
    chat: ChatUI,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.Companion.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            // Аватар
            Box(contentAlignment = Alignment.Companion.BottomEnd) {
                if (chat.participantAvatar != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(chat.participantAvatar)
                            .crossfade(true)
                            .build(),
                        contentScale = ContentScale.Crop,
                        contentDescription = chat.participantName,
                        modifier = Modifier.Companion
                            .size(56.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier.Companion
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6AA26C))
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = chat.participantName,
                            tint = Color.Companion.White,
                            modifier = Modifier.Companion
                                .size(32.dp)
                                .align(Alignment.Companion.Center)
                        )
                    }
                }

                // Онлайн индикатор
                if (chat.isOnline) {
                    Box(
                        modifier = Modifier.Companion
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color.Companion.Green)
                            .border(2.dp, Color.Companion.White, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.Companion.width(16.dp))

            // Информация о чате
            Column(
                modifier = Modifier.Companion.weight(1f)
            ) {
                Row(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    Text(
                        text = chat.participantName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Companion.Bold,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        maxLines = 1,
                        overflow = TextOverflow.Companion.Ellipsis
                    )

                    Text(
                        text = chat.lastMessageTime,
                        fontSize = 12.sp,
                        color = Color.Companion.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
                    )
                }

                Spacer(modifier = Modifier.Companion.height(4.dp))

                Text(
                    text = chat.lastMessage,
                    fontSize = 14.sp,
                    color = Color.Companion.DarkGray,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    maxLines = 1,
                    overflow = TextOverflow.Companion.Ellipsis
                )

                // Информация об объявлении
                if (chat.advertisementTitle != null) {
                    Spacer(modifier = Modifier.Companion.height(4.dp))

                    Surface(
                        modifier = Modifier.Companion
                            .clip(RoundedCornerShape(8.dp)),
                        color = Color(0xFF6AA26C).copy(alpha = 0.1f)
                    ) {
                        Row(
                            modifier = Modifier.Companion.padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            ),
                            verticalAlignment = Alignment.Companion.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Объявление",
                                tint = Color(0xFF6AA26C),
                                modifier = Modifier.Companion.size(12.dp)
                            )

                            Spacer(modifier = Modifier.Companion.width(4.dp))

                            Text(
                                text = chat.advertisementTitle,
                                fontSize = 11.sp,
                                color = Color(0xFF6AA26C),
                                fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                                maxLines = 1,
                                overflow = TextOverflow.Companion.Ellipsis,
                                modifier = Modifier.Companion.weight(1f)
                            )

                            if (chat.advertisementPrice != null) {
                                Spacer(modifier = Modifier.Companion.width(4.dp))

                                Text(
                                    text = "${
                                        NumberFormat.getNumberInstance(Locale.getDefault())
                                            .format(chat.advertisementPrice)
                                    } ₽",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Companion.Bold,
                                    color = Color(0xFF6AA26C),
                                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                                )
                            }
                        }
                    }
                }
            }

            // Непрочитанные сообщения
            if (chat.unreadCount > 0) {
                Spacer(modifier = Modifier.Companion.width(8.dp))

                Box(
                    modifier = Modifier.Companion
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF6AA26C)),
                    contentAlignment = Alignment.Companion.Center
                ) {
                    Text(
                        text = if (chat.unreadCount > 99) "99+" else chat.unreadCount.toString(),
                        color = Color.Companion.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Companion.Bold,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                }
            }
        }
    }
}