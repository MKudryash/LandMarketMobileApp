package com.example.landmarketmobileapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.components.ChatsList
import com.example.landmarketmobileapp.components.NotificationsList
import com.example.landmarketmobileapp.viewModels.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    onNavigationChat: (String, String) -> Unit,
    viewModel: ChatViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val chatState by viewModel.chatsState.collectAsState()
    val notificationState by viewModel.notificationsState.collectAsState()

    val tabs = listOf("Чаты", "Уведомления")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Сообщения",
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6AA26C),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    if (selectedTab == 1 && notificationState.notifications.any { !it.isRead }) {
                        IconButton(
                            onClick = { viewModel.markNotificationsAsRead() }
                        ) {
                            Icon(
                                Icons.Default.Done,
                                contentDescription = "Прочитать все"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {
            // Вкладки
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Color(0xFF6AA26C),
                divider = {
                    Divider(color = Color.Gray.copy(alpha = 0.3f))
                },
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color(0xFF6AA26C),
                        height = 3.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontFamily = FontFamily(Font(R.font.montserrat_medium))
                            )
                        },
                        selectedContentColor = Color(0xFF6AA26C),
                        unselectedContentColor = Color.Gray,
                        icon = {
                            when (index) {
                                0 -> {
                                    Icon(
                                        painterResource(R.drawable.chat),
                                        contentDescription = title,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                1 -> {
                                    val unreadCount = notificationState.notifications.count { !it.isRead }
                                    Box {
                                        Icon(
                                            Icons.Default.Notifications,
                                            contentDescription = title,
                                            modifier = Modifier.size(20.dp)
                                        )

                                        if (unreadCount > 0) {
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .clip(CircleShape)
                                                    .background(Color.Red)
                                                    .align(Alignment.TopEnd)
                                                    .offset(x = 4.dp, y = (-4).dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }

            // Контент вкладок
            when (selectedTab) {
                0 -> ChatsList(
                    chatState = chatState,
                    onChatClick = { chatId, userId -> onNavigationChat(chatId, userId) },
                    onRefresh = { viewModel.loadChats() }
                )

                1 -> NotificationsList(
                    notificationState = notificationState,
                    onRefresh = { viewModel.loadNotifications() }
                )
            }
        }
    }
}

