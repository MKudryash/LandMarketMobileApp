package com.example.landmarketmobileapp.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.landmarketmobileapp.presentation.components.ChatTopBar
import com.example.landmarketmobileapp.presentation.components.MessageInputField
import com.example.landmarketmobileapp.presentation.components.MessagesList
import com.example.landmarketmobileapp.models.state.ChatUI
import com.example.landmarketmobileapp.presentation.viewModels.ChatViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navigateToBack: () -> Unit,
    chatId: String,
    otherId: String,
    viewModel: ChatViewModel = viewModel()
) {
    var messageText by remember { mutableStateOf("") }
    val messagesState by viewModel.messagesState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    Log.d("CHAT SCREEN", otherId,)
    Log.d("CHAT SCREEN", chatId,)
    // Загружаем сообщения при открытии экрана
    LaunchedEffect(chatId) {
        viewModel.loadMessages(chatId, otherId)
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
                        participantName = state?.user ?: "Пользователь",
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
            modifier = Modifier.Companion
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {
            // Список сообщений
            MessagesList(
                messagesState = messagesState,
                listState = listState,
                modifier = Modifier.Companion.weight(1f)
            )
        }
    }
}