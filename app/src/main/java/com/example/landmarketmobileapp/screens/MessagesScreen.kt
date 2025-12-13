package com.example.landmarketmobileapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.models.ChatUI
import com.example.landmarketmobileapp.models.MessageUI
import com.example.landmarketmobileapp.models.NotificationType
import com.example.landmarketmobileapp.models.NotificationUI
import com.example.landmarketmobileapp.viewModels.ChatViewModel
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    onNavigationChat: (String) -> Unit,
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
                    onChatClick = { chatId -> onNavigationChat(chatId) },
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

@Composable
fun ChatsList(
    chatState: com.example.landmarketmobileapp.viewModels.ChatListState,
    onChatClick: (String) -> Unit,
    onRefresh: () -> Unit
) {
    when {
        chatState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF6AA26C))
            }
        }

        chatState.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "Ошибка",
                        tint = Color.Red,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = chatState.error,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_medium))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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

        chatState.chats.isEmpty() -> {
            EmptyChatsState()
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                items(chatState.chats) { chat ->
                    ChatItem(
                        chat = chat,
                        onClick = { onChatClick(chat.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ChatItem(
    chat: ChatUI,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Аватар
            Box(contentAlignment = Alignment.BottomEnd) {
                if (chat.participantAvatar != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(chat.participantAvatar)
                            .crossfade(true)
                            .build(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        contentDescription = chat.participantName,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6AA26C))
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = chat.participantName,
                            tint = Color.White,
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.Center)
                        )
                    }
                }

                // Онлайн индикатор
                if (chat.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color.Green)
                            .border(2.dp, Color.White, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Информация о чате
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chat.participantName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = chat.lastMessageTime,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = chat.lastMessage,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Информация об объявлении
                if (chat.advertisementTitle != null) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp)),
                        color = Color(0xFF6AA26C).copy(alpha = 0.1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Объявление",
                                tint = Color(0xFF6AA26C),
                                modifier = Modifier.size(12.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = chat.advertisementTitle,
                                fontSize = 11.sp,
                                color = Color(0xFF6AA26C),
                                fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )

                            if (chat.advertisementPrice != null) {
                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = "${NumberFormat.getNumberInstance(Locale.getDefault()).format(chat.advertisementPrice)} ₽",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
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
                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF6AA26C)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (chat.unreadCount > 99) "99+" else chat.unreadCount.toString(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyChatsState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painterResource(R.drawable.chat),
                contentDescription = "Нет чатов",
                tint = Color.Gray,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Нет сообщений",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )

            Text(
                text = "Здесь будут отображаться ваши чаты",
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun NotificationsList(
    notificationState: com.example.landmarketmobileapp.viewModels.NotificationsState,
    onRefresh: () -> Unit
) {
    when {
        notificationState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF6AA26C))
            }
        }

        notificationState.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "Ошибка",
                        tint = Color.Red,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = notificationState.error,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_medium))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                items(notificationState.notifications) { notification ->
                    NotificationItem(notification = notification)
                }
            }
        }
    }
}

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
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (!notification.isRead) Color(0xFFE8F5E9) else Color.White
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                icon,
                contentDescription = notification.title,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = notification.time,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.message,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (!notification.isRead) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF6AA26C).copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Новое",
                            fontSize = 10.sp,
                            color = Color(0xFF6AA26C),
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyNotificationsState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Outlined.Notifications,
                contentDescription = "Нет уведомлений",
                tint = Color.Gray,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Нет уведомлений",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )

            Text(
                text = "Здесь будут отображаться ваши уведомления",
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
@Composable
fun MessageInputField(
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onAttachClick: () -> Unit,
    focusRequester: FocusRequester
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Кнопка прикрепления
            IconButton(
                onClick = onAttachClick,
                modifier = Modifier.size(40.dp)
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
                modifier = Modifier
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
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = { onSendClick() }
                )
            )

            // Кнопка отправки
            IconButton(
                onClick = onSendClick,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (messageText.isNotBlank()) Color(0xFF6AA26C) else Color.Gray.copy(alpha = 0.3f)),
                enabled = messageText.isNotBlank()
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Отправить",
                    tint = if (messageText.isNotBlank()) Color.White else Color.Gray
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navigateToBack: () -> Unit,
    chatId: String,
    viewModel: ChatViewModel = viewModel()
) {
    var messageText by remember { mutableStateOf("") }
    val messagesState by viewModel.messagesState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Загружаем сообщения при открытии экрана
    LaunchedEffect(chatId) {
        viewModel.loadMessages(chatId)
    }

    // Прокручиваем к последнему сообщению при загрузке
    LaunchedEffect(messagesState?.messages) {
        messagesState?.let { state ->
            if (state.messages.isNotEmpty()) {
                delay(100)
                listState.animateScrollToItem(state.messages.size - 1)
            }
        }
    }

    // Очищаем состояние при закрытии экрана
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearMessagesState()
        }
    }

    fun sendMessage() {
        if (messageText.isNotBlank()) {
            viewModel.sendMessage(chatId, messageText)
            messageText = ""
            keyboardController?.hide()
        }
    }

    Scaffold(
        topBar = {
            ChatTopBar(
                chat = messagesState.let { state ->
                    ChatUI(
                        id = chatId,
                        participantName = "Загрузка...",
                        lastMessage = "",
                        lastMessageTime = "",
                        unreadCount = 0,
                        isOnline = false,
                        otherUserId = ""
                    )
                },
                onBackClick = navigateToBack,
                onCallClick = { /* Позвонить */ },
                onInfoClick = { /* Информация о чате */ }
            )
        },
        bottomBar = {
            MessageInputField(
                messageText = messageText,
                onMessageTextChange = { messageText = it },
                onSendClick = { sendMessage() },
                onAttachClick = { /* Прикрепить файл */ },
                focusRequester = focusRequester
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {
            // Список сообщений
            MessagesList(
                messagesState = messagesState,
                listState = listState,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(onClick = onInfoClick)
            ) {
                // Аватар
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6AA26C))
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = chat.participantName,
                            tint = Color.White,
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.Center)
                        )
                    }

                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = chat.participantName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF6AA26C),
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
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
@Composable
fun MessagesList(
    messagesState: com.example.landmarketmobileapp.viewModels.MessagesState?,
    listState: androidx.compose.foundation.lazy.LazyListState,
    modifier: Modifier = Modifier
) {
    when {
        messagesState == null || messagesState.isLoading -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF6AA26C))
            }
        }

        messagesState.error != null -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "Ошибка",
                        tint = Color.Red,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = messagesState.error,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_medium))
                    )
                }
            }
        }

        messagesState.messages.isEmpty() -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Нет сообщений",
                    fontSize = 16.sp,
                    color = Color.Gray,
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

@Composable
fun MessageBubble(message: MessageUI) {
    val bubbleColor = if (message.isSentByMe) {
        Color(0xFF6AA26C)
    } else {
        Color.White
    }

    val textColor = if (message.isSentByMe) {
        Color.White
    } else {
        Color.Black
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalAlignment = if (message.isSentByMe) Alignment.End else Alignment.Start
    ) {
        Surface(
            modifier = Modifier
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
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
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
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message.time,
                fontSize = 11.sp,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )

            if (message.isSentByMe) {
                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    if (message.isRead) Icons.Default.Done else Icons.Default.Done,
                    contentDescription = "Статус",
                    tint = if (message.isRead) Color(0xFF6AA26C) else Color.Gray,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}