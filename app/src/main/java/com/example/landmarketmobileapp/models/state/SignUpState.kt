package com.example.landmarketmobileapp.models.state

data class SignUpState(
    val email: String = "",
    val telephone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val username: String = "",
    var isEmailError: Boolean = false
)