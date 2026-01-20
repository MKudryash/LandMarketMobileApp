package com.example.landmarketmobileapp.presentation.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.presentation.components.AuthButton
import com.example.landmarketmobileapp.presentation.components.AuthTextField
import com.example.landmarketmobileapp.presentation.components.AuthTextLink
import com.example.landmarketmobileapp.presentation.viewModels.AuthViewModel
import com.example.landmarketmobileapp.models.state.ResultState
import com.example.landmarketmobileapp.models.state.SignInState
import kotlinx.coroutines.delay

@Composable
fun AuthScreen(
    onNavigateToMain: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    val resultState by authViewModel.resultState.collectAsState()
    val uiState = authViewModel.uiState
    val isAutoLoginInProgress by authViewModel.isAutoLoginInProgress.collectAsState()
    val context = LocalContext.current
    val isInitialCheckInProgress by authViewModel.isInitialCheckInProgress.collectAsState()


    LaunchedEffect(Unit) {
        authViewModel.performInitialCheck(context)
    }

    // Обработка успешного входа (автоматического или обычного)
    LaunchedEffect(resultState) {
        if (resultState is ResultState.Success) {
            // Добавляем небольшую задержку для отображения успеха
            delay(500)
            onNavigateToMain()
        }
    }

    // Определяем, что показывать
    val showAuthScreen = !isInitialCheckInProgress && !isAutoLoginInProgress &&
            resultState !is ResultState.Success
    val showLoading = isInitialCheckInProgress || isAutoLoginInProgress ||
            resultState is ResultState.Success

    // Если выполняется автоматический вход, показываем только индикатор загрузки
    // Показываем либо экран загрузки, либо экран авторизации
    if (showLoading) {
        LoadingScreen(
            message = when {
                isInitialCheckInProgress -> "Проверка сохраненных данных..."
                isAutoLoginInProgress -> "Автоматический вход..."
                resultState is ResultState.Success -> "Успешный вход..."
                else -> "Загрузка..."
            }
        )
    } else if (showAuthScreen) {
        AuthContent(
            uiState = uiState,
            resultState = resultState,
            context = context,
            authViewModel = authViewModel,
            onNavigateToSignUp = onNavigateToSignUp,
            onNavigateToMain
        )
    }

    // Обычный экран входа

}

@Composable
fun AuthContent(
    uiState: SignInState,
    resultState: ResultState,
    context: Context,
    authViewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit,
    onNavigateToMain:()-> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9D9D9))
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Логотип
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_app),
                contentDescription = "Логотип",
                tint = Color(0xFF6AA26C),
                modifier = Modifier.size(120.dp)
            )
        }

        // Заголовок
        Text(
            text = "Вход в аккаунт",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6AA26C),
            modifier = Modifier.padding(bottom = 16.dp),
            fontFamily = FontFamily(Font(R.font.montserrat_bold))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Поле email
        AuthTextField(
            value = uiState.email,
            onValueChange = { authViewModel.updateState(uiState.copy(email = it)) },
            label = "Email",
            keyboardType = KeyboardType.Email,
            isPassword = false
        )

        // Поле пароля
        AuthTextField(
            value = uiState.password,
            onValueChange = { authViewModel.updateState(uiState.copy(password = it)) },
            label = "Пароль",
            keyboardType = KeyboardType.Password,
            isPassword = true
        )

        // Чекбокс "Запомнить меня"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(1.dp)
                ) {
                    Checkbox(
                        checked = uiState.rememberMe,
                        onCheckedChange = {
                            authViewModel.updateState(uiState.copy(rememberMe = it))
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF6AA26C),
                            uncheckedColor = Color.Gray,
                            checkmarkColor = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Запомнить меня",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular))
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        when (resultState) {
            is ResultState.Error -> {
                AuthButton(
                    text = "Войти",
                    onClick = {
                        authViewModel.signIn(context)
                    }
                )
                Text(
                    text = (resultState as ResultState.Error).message,
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    modifier = Modifier.fillMaxWidth().padding(10.dp)
                )
            }

            is ResultState.Initialized -> {
                AuthButton(
                    text = "Войти",
                    onClick = {
                        authViewModel.signIn(context)
                    }
                )
            }

            ResultState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF6AA26C))
                }
            }

            is ResultState.Success -> {
                // Если успешный вход, переходим на главный экран
                LaunchedEffect(Unit) {
                    onNavigateToMain()
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF6AA26C))
                }
            }
        }

        AuthTextLink(
            prefixText = "Еще нет аккаунта?",
            linkText = "Зарегистрироваться",
            onClick = onNavigateToSignUp
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview
@Composable
fun AuthScreenPreview() {
    AuthScreen({}, {})
}

