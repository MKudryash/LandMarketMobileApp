package com.example.landmarketmobileapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.landmarketmobileapp.R

@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onChangePassword: (oldPassword: String, newPassword: String) -> Unit
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val passwordsMatch = newPassword == confirmPassword && newPassword.isNotEmpty()
    val isValid = oldPassword.isNotEmpty() && newPassword.isNotEmpty() && passwordsMatch

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Companion.White)
        ) {
            Column(
                modifier = Modifier.Companion.padding(16.dp)
            ) {
                Text(
                    text = "Смена пароля",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    modifier = Modifier.Companion.padding(bottom = 16.dp)
                )

                // Старый пароль
                AuthTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = "Текущий пароль",
                    keyboardType = KeyboardType.Companion.Password,
                    isPassword = true,
                    modifier = Modifier.Companion.padding(bottom = 12.dp)
                )

                // Новый пароль
                AuthTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = "Новый пароль",
                    keyboardType = KeyboardType.Companion.Password,
                    isPassword = true,
                    modifier = Modifier.Companion.padding(bottom = 12.dp)
                )

                // Подтверждение пароля
                AuthTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Подтвердите пароль",
                    keyboardType = KeyboardType.Companion.Password,
                    isPassword = true,
                    modifier = Modifier.Companion.padding(bottom = 16.dp)
                )

                if (newPassword.isNotEmpty() && confirmPassword.isNotEmpty() && !passwordsMatch) {
                    Text(
                        text = "Пароли не совпадают",
                        color = Color.Companion.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.Companion.padding(bottom = 8.dp)
                    )
                }

                Row(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.Companion.weight(1f)
                    ) {
                        Text("Отмена")
                    }

                    Spacer(modifier = Modifier.Companion.width(8.dp))

                    Button(
                        onClick = {
                            if (isValid) {
                                onChangePassword(oldPassword, newPassword)
                            }
                        },
                        modifier = Modifier.Companion.weight(1f),
                        enabled = isValid,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6AA26C)
                        )
                    ) {
                        Text("Сменить")
                    }
                }
            }
        }
    }
}