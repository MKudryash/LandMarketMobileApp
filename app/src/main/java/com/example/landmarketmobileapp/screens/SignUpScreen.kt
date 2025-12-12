package com.example.landmarketmobileapp.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R

@Composable
fun SignUpScreen(onNavigateToAuth: () -> Unit, onNavigateToMain: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val passwordsMatch = password == confirmPassword && password.isNotEmpty()
    val isFormValid = name.isNotBlank() &&
            email.isNotBlank() &&
            phone.isNotBlank() &&
            password.isNotBlank() &&
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
            value = name,
            onValueChange = { name = it },
            label = "ФИО",
            keyboardType = KeyboardType.Text,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Поле email
        AuthTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            keyboardType = KeyboardType.Email,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Поле телефона
        AuthTextField(
            value = phone,
            onValueChange = { phone = it },
            label = "Телефон",
            keyboardType = KeyboardType.Phone,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Поле пароля
        AuthTextField(
            value = password,
            onValueChange = { password = it },
            label = "Пароль",
            keyboardType = KeyboardType.Password,
            isPassword = true,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Поле подтверждения пароля
        AuthTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Подтвердите пароль",
            keyboardType = KeyboardType.Password,
            isPassword = true,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Валидация пароля
        if (password.isNotBlank() && confirmPassword.isNotBlank() && !passwordsMatch) {
            Text(
                text = "Пароли не совпадают",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )
        }

        if (password.isNotEmpty() && password.length < 6) {
            Text(
                text = "Пароль должен содержать минимум 6 символов",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )
        }

        // Кнопка регистрации
        AuthButton(
            text = "Зарегистрироваться",
            onClick = {
                // Здесь логика регистрации
                if (isFormValid) {
                    onNavigateToMain()
                }
            },
            enabled = isFormValid,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

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