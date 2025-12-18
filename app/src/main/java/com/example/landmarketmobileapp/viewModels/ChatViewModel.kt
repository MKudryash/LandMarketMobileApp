// viewModels/ChatViewModel.kt
package com.example.landmarketmobileapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.landmarketmobileapp.data.Constants.supabase
import com.example.landmarketmobileapp.models.*
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Objects.isNull

class ChatViewModel : ViewModel() {

    // Состояния для списка чатов
    private val _chatsState = MutableStateFlow(ChatListState())
    val chatsState: StateFlow<ChatListState> = _chatsState.asStateFlow()

    // Состояния для сообщений
    private val _messagesState = MutableStateFlow<MessagesState?>(null)
    val messagesState: StateFlow<MessagesState?> = _messagesState.asStateFlow()

    // Состояния для уведомлений
    private val _notificationsState = MutableStateFlow(NotificationsState())
    val notificationsState: StateFlow<NotificationsState> = _notificationsState.asStateFlow()

    // Результаты операций
    private val _resultState = MutableStateFlow<ResultState>(ResultState.Initialized)
    val resultState: StateFlow<ResultState> = _resultState.asStateFlow()

    private val currentUserId get() = supabase.auth.currentUserOrNull()?.id

    // Форматтеры для дат
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM")
    private val isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    init {
        // Загружаем чаты при инициализации
        loadChats()
        loadNotifications()
    }

    /**
     * Загрузка списка чатов текущего пользователя
     */
    fun loadChats() {
        val userId = currentUserId ?: run {
            _chatsState.value = _chatsState.value.copy(
                isLoading = false,
                error = "Пользователь не авторизован"
            )
            return
        }
Log.d("MY ID", userId)
        _chatsState.value = _chatsState.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                // Получаем чаты где пользователь является участником
                val chats = supabase.postgrest
                    .from("chats")
                    .select(
                        columns = Columns.list(
                            "id",
                            "participant1_id",
                            "participant2_id",
                            "advertisement_id",
                            "last_message_id",
                            "last_message_at",
                            "unread_count_p1",
                            "unread_count_p2",
                            "created_at"
                        )
                    ) {
                        filter {
                            or {
                                eq("participant1_id", userId)
                                eq("participant2_id", userId)
                            }
                        }
                       /* order("last_message_time", ascending = false)*/
                    }
                    .decodeList<Chat>()

                // Собираем все ID пользователей, чьи данные нам нужны
                val userIds = chats.flatMap { chat ->
                    listOfNotNull(
                        chat.user1Id,
                        chat.user2Id
                    )
                }.distinct()

                // Загружаем данные пользователей
                val users = if (userIds.isNotEmpty()) {
                    supabase.postgrest
                        .from("users")
                        .select(

                        ) {
                            filter { isIn("id", userIds) }
                        }
                        .decodeList<Profile>()
                } else {
                    emptyList()
                }

                val userMap = users.associateBy { it.id }

                // Загружаем данные объявлений
                val advertisementIds = chats.mapNotNull { it.advertisementId }.distinct()
                val advertisements = if (advertisementIds.isNotEmpty()) {
                    supabase.postgrest
                        .from("advertisements")
                        .select(
                            columns = Columns.list("id", "title", "price")
                        ) {
                            filter { isIn("id", advertisementIds) }
                        }
                        .decodeList<Advertisement>()
                } else {
                    emptyList()
                }



                val advertisementMap = advertisements.associateBy { it.id }





                // Преобразуем в UI состояние
                val chatUIs = chats.map { chat ->
                    val isUser1 = chat.user1Id == userId
                    val otherUserId = if (isUser1) chat.user2Id else chat.user1Id
                    val otherUser = userMap[otherUserId]
                    val unreadCount = if (isUser1) chat.user1UnreadCount else chat.user2UnreadCount
                    val advertisement = chat.advertisementId?.let { advertisementMap[it] }
                    val message = supabase.postgrest
                        .from("messages")
                        .select {
                            filter { eq("id", chat.lastMessage!!) }
                            /*   order"created_at", ascending = true)*/
                        }
                        .decodeSingle<ChatMessage>()
                    ChatUI(
                        id = chat.id,
                        participantName = otherUser?.fullName ?: "Пользователь",
                        participantAvatar = otherUser?.image,
                        lastMessage = message.message,
                        lastMessageTime = formatMessageTime(chat.lastMessageTime),
                        unreadCount = unreadCount ?: 0,
                        isOnline = false, // Здесь нужно получить статус из presence
                        advertisementTitle = advertisement?.title,
                        advertisementPrice = advertisement?.price?.toInt(),
                        otherUserId = otherUserId
                    )
                }

                _chatsState.value = ChatListState(
                    chats = chatUIs,
                    isLoading = false,
                    error = null
                )

                _resultState.value = ResultState.Success("Чаты загружены")

            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error loading chats", e)
                _chatsState.value = _chatsState.value.copy(
                    isLoading = false,
                    error = "Не удалось загрузить чаты: ${e.message}"
                )
                _resultState.value = ResultState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }

    /**
     * Загрузка сообщений в чате
     */
    fun loadMessages(chatId: String, otherUserId: String) {
        val userId = currentUserId ?: return
        Log.d("loadMessages", "Chat ID: $chatId")
        Log.d("loadMessages", "Other User ID: $otherUserId")

        _messagesState.value = MessagesState(
            chatId = chatId,
            isLoading = true,
            messages = emptyList(),
        )

        viewModelScope.launch {
            try {
                // Загружаем сообщения
                val messages = supabase.postgrest
                    .from("messages")
                    .select {
                        filter { eq("chat_id", chatId) }
                    }
                    .decodeList<ChatMessage>()

                // Преобразуем в UI состояние
                val messageUIs = messages.map { message ->
                    MessageUI(
                        id = message.id,
                        text = message.message,
                        time = formatMessageTime(message.createdAt),
                        isSentByMe = message.senderId == userId,
                        isRead = message.isRead,
                        type = message.type
                    )
                }

                Log.d("ChatViewModel", "Loaded ${messages.size} messages")

                // Получаем информацию о собеседнике
                val userInfo = getSellerInfo(otherUserId)
                Log.d("ChatViewModel", "User info: $userInfo")

                // Обновляем состояние
                _messagesState.value = MessagesState(
                    chatId = chatId,
                    isLoading = false,
                    messages = messageUIs,
                    error = null,
                    user = userInfo // Сохраняем имя пользователя
                )

                // Помечаем сообщения как прочитанные
                markMessagesAsRead(chatId)

            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error loading messages", e)
                _messagesState.value = _messagesState.value?.copy(
                    isLoading = false,
                    error = "Не удалось загрузить сообщения: ${e.message}"
                )
            }
        }
    }

    /**
     * Отправка нового сообщения
     */
    fun sendMessage(chatId: String, text: String) {
        val userId = currentUserId ?: return
        val currentTime = LocalDateTime.now().toString()

        viewModelScope.launch {
            try {
                // 1. Сначала сохраняем сообщение
                val messageId = UUID.randomUUID().toString()
                val newMessage = Message(
                    messageId,
                     chatId,
                    userId,
                    userId,
                     text,
                    "text",
                    false,
                     currentTime
                )

                // Сохраняем сообщение в БД
                supabase.postgrest
                    .from("messages")
                    .insert(newMessage)

                // 2. Получаем данные чата
                val chatsResponse = supabase.postgrest
                    .from("chats")
                    .select(
                        columns = Columns.list(
                            "id",
                            "participant1_id",
                            "participant2_id",
                            "unread_count_p1",
                            "unread_count_p2"
                        )
                    ) {
                        filter { eq("id", chatId) }
                    }
                    .decodeList<Chat>()

                if (chatsResponse.isEmpty()) {
                    throw Exception("Чат не найден")
                }

                val chat = chatsResponse[0]
                val isParticipant1 = chat.user1Id == userId

                // 3. Обновляем счетчики непрочитанных
                val updateData = mutableMapOf<String, Any>(
                    "last_message_id" to messageId, // Сохраняем ID сообщения, а не текст
                    "last_message_at" to currentTime
                )



                var count1 =  chat.user1UnreadCount+1
                var count2 =  chat.user2UnreadCount+1
                // Обновляем счетчик непрочитанных
                if (isParticipant1) {
                    // Если сообщение отправил user1, увеличиваем счетчик у user2
                    supabase.postgrest.from("chats")
                        .update(
                            mapOf(
                                "unread_count_p1" to count1  // Передаем новое значение
                            )
                        ) {
                            filter { eq("id", chatId) }
                        }


                } else {
                    // Если сообщение отправил user2, увеличиваем счетчик у user1
                    supabase.postgrest.from("chats")
                        .update(
                            mapOf(
                                "unread_count_p2" to count2  // Передаем новое значение
                            )
                        ) {
                            filter { eq("id", chatId) }
                        }
                }

                // 5. Загружаем созданное сообщение для UI
                val savedMessage = supabase.postgrest
                    .from("messages")
                    .select {
                        filter { eq("id", messageId) }
                    }
                    .decodeSingle<ChatMessage>()

                // 6. Обновляем локальное состояние
                val newMessageUI = MessageUI(
                    id = savedMessage.id,
                    text = savedMessage.message ?: text,
                    time = formatMessageTime(savedMessage.createdAt),
                    isSentByMe = true,
                    isRead = false,
                    type = MessageType.TEXT
                )

                _messagesState.value?.let { currentState ->
                    _messagesState.value = currentState.copy(
                        messages = currentState.messages + newMessageUI
                    )
                }

                // 7. Обновляем список чатов
                loadChats()

                _resultState.value = ResultState.Success("Сообщение отправлено")

            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error sending message", e)
                _messagesState.value = _messagesState.value?.copy(
                    error = "Не удалось отправить сообщение: ${e.message}"
                )
                _resultState.value = ResultState.Error("Ошибка отправки: ${e.message}")
            }
        }
    }
    private suspend fun getSellerInfo(userId: String?): String {
        if (userId == null) return ""

        return try {
            val user = supabase.postgrest
                .from("users")
                .select {
                    filter { eq("id", userId) }
                }
                .decodeSingle<Profile>()
            user.fullName
        } catch (e: Exception) {
            SellerInfo()
            ""
        }
    }
    /**
     * Создание нового чата
     */
    fun createChat(otherUserId: String, advertisementId: String? = null) {
        val userId = currentUserId ?: return

        viewModelScope.launch {
            try {
                // Проверяем, есть ли уже чат
                val existingChats = supabase.postgrest
                    .from("chats")
                    .select {
                        filter {
                            or {
                                and {
                                    eq("participant1_id", userId)
                                    eq("participant2_id", otherUserId)
                                }
                                and {
                                    eq("participant1_id", otherUserId)
                                    eq("participant2_id", userId)
                                }
                            }
                        }
                        if (advertisementId != null) {
                            filter { eq("advertisement_id", advertisementId) }
                        } else {
                            filter { isNull("advertisement_id") }
                        }
                    }
                    .decodeList<Chat>()

                if (existingChats.isNotEmpty()) {
                    // Чат уже существует
                    _resultState.value = ResultState.Success("Чат уже существует")
                    return@launch
                }

                // Создаем новый чат
                val newChat = mapOf(
                    "id" to UUID.randomUUID().toString(),
                    "participant1_id" to userId,
                    "participant2_id" to otherUserId,
                    "advertisement_id" to advertisementId,
                    "last_message_at" to LocalDateTime.now().toString(),
                    "unread_count_p1" to 0,
                    "unread_count_p2" to 0,
                    "created_at" to LocalDateTime.now().toString()
                )

                supabase.postgrest
                    .from("chats")
                    .insert(newChat)

                _resultState.value = ResultState.Success("Чат создан")
                loadChats() // Обновляем список чатов

            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error creating chat", e)
                _resultState.value = ResultState.Error("Ошибка создания чата: ${e.message}")
            }
        }
    }

    /**
     * Загрузка уведомлений
     */
    fun loadNotifications() {
        val userId = currentUserId ?: return

        _notificationsState.value = _notificationsState.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val notifications = supabase.postgrest
                    .from("notifications")
                    .select {
                        filter { eq("user_id", userId) }

                        limit(50)
                    }
                    .decodeList<Notification>()

                val notificationUIs = notifications.map { notification ->
                    NotificationUI(
                        id = notification.id,
                        title = notification.title,
                        message = notification.message,
                        time = formatMessageTime(notification.createdAt),
                        isRead = notification.isRead,
                        type = notification.type
                    )
                }

                _notificationsState.value = NotificationsState(
                    notifications = notificationUIs,
                    isLoading = false,
                    error = null
                )

            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error loading notifications", e)
                _notificationsState.value = _notificationsState.value.copy(
                    isLoading = false,
                    error = "Не удалось загрузить уведомления"
                )
            }
        }
    }

    /**
     * Пометить уведомления как прочитанные
     */
    fun markNotificationsAsRead() {
        val userId = currentUserId ?: return

        viewModelScope.launch {
            try {
                supabase.postgrest
                    .from("notifications")
                    .update(mapOf("is_read" to true)) {
                        filter { eq("user_id", userId) }
                        filter { eq("is_read", false) }
                    }

                // Обновляем локальное состояние
                _notificationsState.value = _notificationsState.value.copy(
                    notifications = _notificationsState.value.notifications.map {
                        it.copy(isRead = true)
                    }
                )

            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error marking notifications as read", e)
            }
        }
    }

    /**
     * Пометить сообщения как прочитанные
     */
    private fun markMessagesAsRead(chatId: String) {
        val userId = currentUserId ?: return
        Log.d("ChatViewModel", "Marking messages as read for chat $chatId")

        viewModelScope.launch {
            try {
                // 1. Получаем информацию о чате с явным указанием колонок
                val chats = supabase.postgrest
                    .from("chats")
                    .select(
                        columns = Columns.list(
                            "id",
                            "participant1_id",
                            "participant2_id",
                            "unread_count_p1",
                            "unread_count_p2"
                        )
                    ) {
                        filter { eq("id", chatId) }
                    }
                    .decodeList<Chat>()

                if (chats.isEmpty()) {
                    Log.d("ChatViewModel", "Chat not found: $chatId")
                    return@launch
                }

                val chat = chats[0]
                Log.d("ChatViewModel", "Found chat: ${chat.id}, participant1: ${chat.user1Id}, participant2: ${chat.user2Id}")

                // 2. Определяем, кто из участников чата является текущим пользователем
                val isParticipant1 = chat.user1Id == userId
                val updateField = if (isParticipant1) "unread_count_p1" else "unread_count_p2"
                val updateValue = 0

                Log.d("ChatViewModel", "User is participant1: $isParticipant1, updating field: $updateField to $updateValue")

                // 3. Обнуляем счетчик непрочитанных для текущего пользователя
                supabase.postgrest
                    .from("chats")
                    .update(mapOf(updateField to updateValue)) {
                        filter { eq("id", chatId) }
                    }

                // 4. Помечаем сообщения как прочитанные в БД
                // Определяем ID другого участника
                val otherUserId = if (isParticipant1) chat.user2Id else chat.user1Id

                if (otherUserId != null) {
                    // Помечаем сообщения от другого пользователя как прочитанные
                    supabase.postgrest
                        .from("messages")
                        .update(mapOf("is_read" to true)) {
                            filter { eq("chat_id", chatId) }
                            filter { eq("sender_id", otherUserId) }
                            filter { eq("is_read", false) }
                        }
                    Log.d("ChatViewModel", "Marked messages from user $otherUserId as read")
                } else {
                    // Если не можем определить другого пользователя, помечаем все сообщения в чате
                    // (кроме собственных) как прочитанные
                    supabase.postgrest
                        .from("messages")
                        .update(mapOf("is_read" to true)) {
                            filter { eq("chat_id", chatId) }
                            filter { neq("sender_id", userId) } // Не свои сообщения
                            filter { eq("is_read", false) }
                        }
                    Log.d("ChatViewModel", "Marked all non-own messages in chat as read")
                }

                // 5. Обновляем локальное состояние сообщений
                _messagesState.value?.let { currentState ->
                    val updatedMessages = currentState.messages.map { message ->
                        if (!message.isSentByMe) {
                            message.copy(isRead = true)
                        } else {
                            message
                        }
                    }
                    _messagesState.value = currentState.copy(messages = updatedMessages)
                    Log.d("ChatViewModel", "Updated ${updatedMessages.size} messages in local state")
                }

                // 6. Обновляем список чатов для отображения изменений
                loadChats()

                Log.d("ChatViewModel", "Successfully marked messages as read")

            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error marking messages as read", e)

                // Упрощенная логика в случае ошибки
                try {
                    // Просто обнуляем оба счетчика на всякий случай
                    supabase.postgrest
                        .from("chats")
                        .update(
                            mapOf(
                                "unread_count_p1" to 0,
                                "unread_count_p2" to 0
                            )
                        ) {
                            filter { eq("id", chatId) }
                        }
                    Log.d("ChatViewModel", "Reset both unread counters as fallback")
                } catch (fallbackError: Exception) {
                    Log.e("ChatViewModel", "Fallback also failed", fallbackError)
                }
            }
        }
    }

    /**
     * Форматирование времени сообщения
     */
    private fun formatMessageTime(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return ""

        return try {
            val dateTime = LocalDateTime.parse(dateString, isoFormatter)
            val today = LocalDateTime.now()

            return when {
                dateTime.toLocalDate() == today.toLocalDate() ->
                    dateTime.format(timeFormatter)
                dateTime.toLocalDate() == today.minusDays(1).toLocalDate() ->
                    "Вчера"
                else ->
                    dateTime.format(dateFormatter)
            }
        } catch (e: Exception) {
            dateString
        }
    }

    /**
     * Очистка состояния сообщений
     */
    fun clearMessagesState() {
        _messagesState.value = null
    }

    /**
     * Обновление одного чата в списке
     */
    fun updateChatInList(updatedChat: ChatUI) {
        _chatsState.value = _chatsState.value.copy(
            chats = _chatsState.value.chats.map { chat ->
                if (chat.id == updatedChat.id) updatedChat else chat
            }
        )
    }
}

// Состояния UI
data class ChatListState(
    val chats: List<ChatUI> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class MessagesState(
    val chatId: String,
    val messages: List<MessageUI> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: String? = "Пользователь"
)

data class NotificationsState(
    val notifications: List<NotificationUI> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)