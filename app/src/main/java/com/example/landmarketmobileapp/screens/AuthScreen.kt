package com.example.landmarketmobileapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.viewModels.AuthViewModel
import com.example.landmarketmobileapp.viewModels.ResultState
import kotlinx.datetime.Month

@Composable
fun AuthScreen(onNavigateToMain: () -> Unit, onNavigateToSignUp: () -> Unit,
               authViewModel: AuthViewModel = viewModel()) {
    val resultState by authViewModel.resultState.collectAsState() // использует collectAsState() для преобразования потока состояний (Flow<ResultState>) из ViewModel в состояние
    val uiState = authViewModel.uiState

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
            isPassword = true,
            modifier = Modifier.padding(bottom = 24.dp)
        )



        Spacer(modifier = Modifier.height(32.dp))
        when (resultState) {
            is ResultState.Error -> {
                AuthButton(
                    text = "Войти",
                    onClick = {
                        authViewModel.signIn()
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
                        authViewModel.signIn()
                    }
                )
            }

            ResultState.Loading -> {
                Box(
                    Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center

               )
                {
                    CircularProgressIndicator( )
                }
            }

            is ResultState.Success -> {
                onNavigateToMain()
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
@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                label,
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )
        },
        trailingIcon = {
            if (isPassword) {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisible) {
                                R.drawable.eye_close // Иконка скрытого пароля
                            } else {
                                R.drawable.eye_open // Иконка видимого пароля
                            }
                        ),
                        contentDescription = if (passwordVisible) {
                            "Скрыть пароль"
                        } else {
                            "Показать пароль"
                        },
                        tint = Color.DarkGray.copy(alpha = 0.5f)
                    )
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (!passwordVisible && isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true
    )
}
@Composable
fun AuthButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = Color(0xFF6AA26C),
    textColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = Color.White
        ),
        enabled = enabled
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily(Font(R.font.montserrat_bold))
        )
    }
}
@Composable
fun AuthTextLink(
    prefixText: String,
    linkText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = prefixText,
            fontSize = 16.sp,
            color = Color.Gray,
            fontFamily = FontFamily(Font(R.font.montserrat_regular))
        )
        TextButton(
            onClick = onClick,
            modifier = Modifier.padding(start = 4.dp)
        ) {
            Text(
                text = linkText,
                fontSize = 16.sp,
                color = Color(0xFF6AA26C),
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )
        }
    }
}