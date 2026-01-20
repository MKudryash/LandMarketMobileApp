package com.example.landmarketmobileapp.models.state

data class SignInState(
    val email: String = "t@t.ru",
    val password: String = "1",
    var errorEmail: Boolean = false,
    val errorPassword: Boolean = false,
    val rememberMe: Boolean = false // Добавлено поле для запоминания данных
)