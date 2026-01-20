package com.example.landmarketmobileapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun SignUpScreen(onNavigateToAuth: () -> Unit, onNavigateToMain: () -> Unit,
                 authViewModel: AuthViewModel = viewModel()
) {

    val resultState by authViewModel.resultStateSignUp.collectAsState() // использует collectAsState() для преобразования потока состояний (Flow<ResultState>) из ViewModel в состояние
    val uiState = authViewModel.uiStateSignUp

    val passwordsMatch = uiState.password == uiState.confirmPassword && uiState.password.isNotEmpty()
    val isFormValid = uiState.username.isNotBlank() &&
            uiState.email.isNotBlank() &&
            uiState.telephone.isNotBlank() &&
            uiState.password.isNotBlank() &&
            passwordsMatch



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
                .height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_app),
                contentDescription = "Логотип",
                tint = Color(0xFF6AA26C),
                modifier = Modifier.size(100.dp)
            )
        }

        // Заголовок
        Text(
            text = "Регистрация",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6AA26C),
            modifier = Modifier.padding(bottom = 16.dp),
            fontFamily = FontFamily(Font(R.font.montserrat_bold))
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Поле ФИО
        AuthTextField(
            value = uiState.username,
            onValueChange = { authViewModel.updateState(uiState.copy(username = it)) },
            label = "ФИО",
            keyboardType = KeyboardType.Text,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Поле email
        AuthTextField(
            value = uiState.email,
            onValueChange = { authViewModel.updateState(uiState.copy(email = it)) },
            label = "Email",
            keyboardType = KeyboardType.Email,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Поле телефона
        AuthTextField(
            value = uiState.telephone,
            onValueChange = { authViewModel.updateState(uiState.copy(telephone = it)) },
            label = "Телефон",
            keyboardType = KeyboardType.Phone,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Поле пароля
        AuthTextField(
            value = uiState.password,
            onValueChange = { authViewModel.updateState(uiState.copy(password = it)) },
            label = "Пароль",
            keyboardType = KeyboardType.Password,
            isPassword = true,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Поле подтверждения пароля
        AuthTextField(
            value = uiState.confirmPassword,
            onValueChange = { authViewModel.updateState(uiState.copy(confirmPassword = it)) },
            label = "Подтвердите пароль",
            keyboardType = KeyboardType.Password,
            isPassword = true,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Валидация пароля
        if (uiState.password.isNotBlank() && uiState.confirmPassword.isNotBlank() && !passwordsMatch) {
            Text(
                text = "Пароли не совпадают",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )
        }

        if (uiState.password.isNotEmpty() && uiState.password.length < 6) {
            Text(
                text = "Пароль должен содержать минимум 6 символов",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )
        }

        when (resultState) {
            is ResultState.Error -> {
                AuthButton(
                    text = "Зарегистрироваться",
                    onClick = {
                        if (isFormValid)
                            authViewModel.signUp()
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
                    text = "Зарегистрироваться",
                    onClick = {
                        if (isFormValid)
                            authViewModel.signUp()
                    }
                )
            }

            ResultState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center
                )
                {
                    CircularProgressIndicator(color = Color(0xFF6AA26C))
                }
            }

            is ResultState.Success -> {
                onNavigateToMain()
            }
        }

        // Ссылка на вход
        AuthTextLink(
            prefixText = "Уже есть аккаунт?",
            linkText = "Войти",
            onClick = onNavigateToAuth,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreen({}, {})
}