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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.window.Dialog
import com.example.landmarketmobileapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

// Модель данных для чата
data class Chat(
    val id: String,
    val participantName: String,
    val participantAvatar: String? = null,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false,
    val advertisementTitle: String? = null,
    val advertisementPrice: Int? = null
)

// Модель данных для сообщения
data class Message(
    val id: String,
    val text: String,
    val time: String,
    val isSentByMe: Boolean,
    val isRead: Boolean = true,
    val type: MessageType = MessageType.TEXT
)

enum class MessageType {
    TEXT, IMAGE, DOCUMENT, SYSTEM
}

// Модель данных для системного уведомления
data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean = false,
    val type: NotificationType = NotificationType.INFO
)

enum class NotificationType {
    INFO, SUCCESS, WARNING, ERROR, NEW_MESSAGE, NEW_AD
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    onNavigationChat: (String)-> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf("") }
    var showArchived by remember { mutableStateOf(false) }

    val tabs = listOf("Чаты", "Уведомления")

    // Пример данных
    val chats = remember {
        listOf(
            Chat(
                id = "1",
                participantName = "Александр Петров",
                lastMessage = "Добрый день! Участок еще доступен?",
                lastMessageTime = "10:30",
                unreadCount = 3,
                isOnline = true,
                advertisementTitle = "Участок 15 соток",
                advertisementPrice = 750000
            ),
            Chat(
                id = "2",
                participantName = "ООО 'Земельные участки'",
                lastMessage = "Отправили вам документы на почту",
                lastMessageTime = "Вчера",
                unreadCount = 0,
                isOnline = false,
                advertisementTitle = "Земля под ИЖС",
                advertisementPrice = 450000
            ),
            Chat(
                id = "3",
                participantName = "Мария Иванова",
                lastMessage = "Спасибо за информацию!",
                lastMessageTime = "15 апр",
                unreadCount = 0,
                isOnline = true,
                advertisementTitle = "Участок в деревне",
                advertisementPrice = 650000
            ),
            Chat(
                id = "4",
                participantName = "Сергей Смирнов",
                lastMessage = "Когда сможете показать участок?",
                lastMessageTime = "12 апр",
                unreadCount = 1,
                isOnline = false
            ),
            Chat(
                id = "5",
                participantName = "Застройщик 'Солнечный'",
                lastMessage = "У нас действует акция до конца месяца",
                lastMessageTime = "10 апр",
                unreadCount = 0,
                isOnline = false,
                advertisementTitle = "Участок в агрогородке",
                advertisementPrice = 1200000
            )
        )
    }

    val notifications = remember {
        listOf(
            Notification(
                id = "1",
                title = "Новое сообщение",
                message = "Александр Петров написал вам сообщение",
                time = "10:30",
                isRead = false,
                type = NotificationType.NEW_MESSAGE
            ),
            Notification(
                id = "2",
                title = "Объявление одобрено",
                message = "Ваше объявление 'Участок 12 соток' прошло модерацию",
                time = "Вчера, 14:20",
                isRead = true,
                type = NotificationType.SUCCESS
            ),
            Notification(
                id = "3",
                title = "Просмотр объявления",
                message = "Ваше объявление просмотрели 15 раз за сегодня",
                time = "Вчера, 12:45",
                isRead = true,
                type = NotificationType.INFO
            ),
            Notification(
                id = "4",
                title = "Новое предложение",
                message = "Похожий участок добавлен в каталог",
                time = "15 апр, 18:30",
                isRead = true,
                type = NotificationType.NEW_AD
            ),
            Notification(
                id = "5",
                title = "Внимание!",
                message = "Завтра истекает срок публикации объявления",
                time = "15 апр, 10:15",
                isRead = true,
                type = NotificationType.WARNING
            )
        )
    }

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
                    IconButton(
                        onClick = { /* Поиск */ }
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Поиск")
                    }

                    IconButton(
                        onClick = { showArchived = !showArchived }
                    ) {
                        Icon(
                           painter = painterResource(R.drawable.archive),
                            contentDescription = "Архив"
                        )
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
                            if (index == 1 && notifications.any { !it.isRead }) {
                                Box {
                                    Icon(
                                        Icons.Default.Notifications,
                                        contentDescription = title,
                                        modifier = Modifier.size(20.dp)
                                    )

                                    // Бейдж непрочитанных
                                    if (notifications.count { !it.isRead } > 0) {
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
                            } else {
                                if (index == 0) {
                                    Icon(
                                        painterResource(R.drawable.chat),
                                        contentDescription = title,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                else {
                                    Icon(
                                         Icons.Default.Notifications,
                                        contentDescription = title,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    )
                }
            }

            // Контент вкладок
            when (selectedTab) {
                0 -> ChatsList(
                    chats = chats.filter { chat ->
                        val matchesSearch = searchText.isEmpty() ||
                                chat.participantName.contains(searchText, ignoreCase = true) ||
                                chat.lastMessage.contains(searchText, ignoreCase = true)
                        matchesSearch && (!showArchived || chat.unreadCount == 0)
                    },
                    onChatClick = { chatId ->
                        onNavigationChat(chatId)
                    }
                )

                1 -> NotificationsList(
                    notifications = notifications,
                    onNotificationClick = { notificationId ->
                        // Обработка клика на уведомление
                    },
                    onMarkAllAsRead = {
                        // Пометить все как прочитанные
                    }
                )
            }
        }
    }
}

@Composable
fun ChatsList(
    chats: List<Chat>,
    onChatClick: (String) -> Unit
) {
    if (chats.isEmpty()) {
        EmptyChatsState()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(chats) { chat ->
                ChatItem(
                    chat = chat,
                    onClick = { onChatClick(chat.id) }
                )
            }
        }
    }
}

@Composable
fun ChatItem(
    chat: Chat,
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

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* Начать новый чат */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6AA26C)
                )
            ) {
                Text("Написать сообщение")
            }
        }
    }
}

@Composable
fun NotificationsList(
    notifications: List<Notification>,
    onNotificationClick: (String) -> Unit,
    onMarkAllAsRead: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (notifications.any { !it.isRead }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Непрочитанные: ${notifications.count { !it.isRead }}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.montserrat_medium))
                )

                TextButton(onClick = onMarkAllAsRead) {
                    Text("Прочитать все")
                }
            }
        }

        if (notifications.isEmpty()) {
            EmptyNotificationsState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                items(notifications) { notification ->
                    NotificationItem(
                        notification = notification,
                        onClick = { onNotificationClick(notification.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: Notification,
    onClick: () -> Unit
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
        NotificationType.NEW_MESSAGE -> Icons.Default.Notifications
        NotificationType.NEW_AD -> Icons.Default.Home
        else -> Icons.Default.Info
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navigateToBack:()->Unit,
    chatId: String
) {
    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(emptyList<Message>()) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Пример данных чата
    val chat = remember {
        Chat(
            id = chatId,
            participantName = "Александр Петров",
            lastMessage = "Добрый день! Участок еще доступен?",
            lastMessageTime = "10:30",
            unreadCount = 3,
            isOnline = true,
            advertisementTitle = "Участок 15 соток в коттеджном посёлке",
            advertisementPrice = 750000
        )
    }

    // Пример сообщений
    LaunchedEffect(chatId) {
        // Загрузка сообщений из базы данных
        messages = listOf(
            Message(
                id = "1",
                text = "Здравствуйте! Заинтересовал ваш участок. Можно подробнее о коммуникациях?",
                time = "10:15",
                isSentByMe = false
            ),
            Message(
                id = "2",
                text = "Добрый день! Конечно, расскажу. На участке проведено электричество, есть центральный водопровод, газ по границе участка, дорога асфальтированная.",
                time = "10:18",
                isSentByMe = true
            ),
            Message(
                id = "3",
                text = "Отлично! А кадастровые документы в порядке?",
                time = "10:20",
                isSentByMe = false
            ),
            Message(
                id = "4",
                text = "Да, все документы готовы. Есть свидетельство о праве собственности, кадастровый паспорт, межевой план. Могу отправить вам сканы.",
                time = "10:22",
                isSentByMe = true
            ),
            Message(
                id = "5",
                text = "Супер! Когда можно приехать посмотреть участок?",
                time = "10:25",
                isSentByMe = false
            ),
            Message(
                id = "6",
                text = "В любое удобное для вас время. Я живу рядом, могу встретить и показать. На этой неделе свободен все дни.",
                time = "10:28",
                isSentByMe = true
            ),
            Message(
                id = "7",
                text = "Хорошо, давайте в субботу в 11:00?",
                time = "10:30",
                isSentByMe = false
            )
        )

        // Прокрутить к последнему сообщению
        delay(100)
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    fun sendMessage() {
        if (messageText.isNotBlank()) {
            val newMessage = Message(
                id = System.currentTimeMillis().toString(),
                text = messageText,
                time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                isSentByMe = true
            )

            messages = messages + newMessage
            messageText = ""

            // Прокрутить к новому сообщению
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }

            // Скрыть клавиатуру
            keyboardController?.hide()
        }
    }

    Scaffold(
        topBar = {
            ChatTopBar(
                chat = chat,
                onBackClick = { navigateToBack()},
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
            // Информация об объявлении
            AdvertisementInfoCard(chat)

            // Список сообщений
            MessagesList(
                messages = messages,
                listState = listState,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    chat: Chat,
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

                    // Онлайн индикатор
                    if (chat.isOnline) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color.Green)
                                .border(2.dp, Color.White, CircleShape)
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

                    Text(
                        text = if (chat.isOnline) "в сети" else "был(а) недавно",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
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
fun AdvertisementInfoCard(chat: Chat) {
    if (chat.advertisementTitle != null) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Переход к объявлению */ }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Изображение объявления
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF6AA26C).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = "Объявление",
                        tint = Color(0xFF6AA26C),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = chat.advertisementTitle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (chat.advertisementPrice != null) {
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${NumberFormat.getNumberInstance(Locale.getDefault()).format(chat.advertisementPrice)} ₽",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6AA26C),
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                    }
                }

                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "Перейти",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun MessagesList(
    messages: List<Message>,
    listState: androidx.compose.foundation.lazy.LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        reverseLayout = false,
        verticalArrangement = Arrangement.Bottom,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(messages) { message ->
            MessageBubble(message = message)
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
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

@Composable
fun NewChatDialog(
    onDismiss: () -> Unit,
    onStartChat: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Заголовок
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Новый чат",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Закрыть")
                    }
                }

                // Поиск
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    placeholder = {
                        Text("Поиск пользователей...")
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Поиск")
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // Список пользователей
                // Здесь должен быть список контактов или недавних собеседников

                Spacer(modifier = Modifier.height(16.dp))

                // Кнопка начала нового чата
                Button(
                    onClick = {
                        onStartChat("new_user")
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6AA26C)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Начать чат")
                }
            }
        }
    }
}

