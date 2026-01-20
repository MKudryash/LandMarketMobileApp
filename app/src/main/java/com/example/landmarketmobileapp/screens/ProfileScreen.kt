package com.example.landmarketmobileapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.components.ChangePasswordDialog
import com.example.landmarketmobileapp.components.ProfileField
import com.example.landmarketmobileapp.viewModels.AuthViewModel
import com.example.landmarketmobileapp.viewModels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    navigateLogOut:()->Unit
) {
    val resultState by viewModel.resultState.collectAsState() // использует collectAsState() для преобразования потока состояний (Flow<ResultState>) из ViewModel в состояние
    val uiState = viewModel.uiState

    val context = LocalContext.current

    var isEditing by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Профиль",
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6AA26C),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Заголовок профиля с аватаром
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF6AA26C))
                    .padding(vertical = 24.dp)
            ) {
                Box(
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        if (uiState.imageUrl != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(uiState.imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Изображение",
                                modifier = Modifier.size(120.dp),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_app),
                                contentDescription = "Аватар",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),
                                tint = Color(0xFF6AA26C)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF6AA26C))
                                .clickable { /* Изменить фото */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.camera),
                                contentDescription = "Изменить фото",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = uiState.username,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )

                Text(
                    text = uiState.email,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    fontFamily = FontFamily(Font(R.font.montserrat_regular))
                )
            }

            // Карточка с информацией
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Заголовок секции
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Личная информация",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )

                        IconButton(
                            onClick = { isEditing = !isEditing }
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Редактировать",
                                tint = Color(0xFF6AA26C)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Поле имени
                    ProfileField(
                        icon = Icons.Default.Person,
                        label = "Имя",
                        value = uiState.username,
                        isEditing = isEditing,
                        onValueChange = { viewModel.updateState(uiState.copy(username = it)) },

                        keyboardType = KeyboardType.Text
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Поле email
                    ProfileField(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = uiState.email,
                        isEditing = isEditing,
                        onValueChange = { viewModel.updateState(uiState.copy(email = it)) },

                        keyboardType = KeyboardType.Email
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Поле телефона
                    ProfileField(
                        icon = Icons.Default.Phone,
                        label = "Телефон",
                        value = uiState.telephone,
                        isEditing = isEditing,
                        onValueChange = { viewModel.updateState(uiState.copy(telephone = it)) },

                        keyboardType = KeyboardType.Phone
                    )

                    if (isEditing) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(
                                onClick = { isEditing = false },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Отмена")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                   viewModel.updateUser()
                                    isEditing = false
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF6AA26C)
                                )
                            ) {
                                Text("Сохранить")
                            }
                        }
                    }
                }
            }

            // Смена пароля
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showChangePasswordDialog = true }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "Смена пароля",
                        tint = Color(0xFF6AA26C),
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Сменить пароль",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                        Text(
                            text = "Обновите свой пароль для безопасности",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.montserrat_regular))
                        )
                    }

                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_forward),
                        contentDescription = "Перейти",
                        tint = Color.Gray
                    )
                }
            }

            // Кнопка выхода
            Button(
                onClick = { authViewModel.clearSavedCredentials(context)
                          navigateLogOut()},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red.copy(alpha = 0.8f)
                )
            ) {
                Text("Выйти из аккаунта")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Диалог смены пароля
    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            onDismiss = { showChangePasswordDialog = false },
            onChangePassword = { oldPass, newPass ->
                viewModel.requestPasswordReset()
                showChangePasswordDialog = false
            }
        )
    }
}


@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navigateLogOut = {})
}