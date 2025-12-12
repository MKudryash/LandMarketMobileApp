package com.example.landmarketmobileapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarWithSettings(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Поиск..."
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Иконка поиска слева
        Icon(
            painter = painterResource(id = R.drawable.search), // или Icons.Default.Search
            contentDescription = "Поиск",
            tint = Color(0xFF6AA26C),
            modifier = Modifier
                .padding(start = 16.dp)
                .size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Поле ввода
        BasicTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            ),
            cursorBrush = SolidColor(Color(0xFF6AA26C)),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (searchText.isEmpty() && !isFocused) {
                        Text(
                            text = placeholder,
                            fontSize = 16.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.montserrat_regular))
                        )
                    }
                    innerTextField()
                }
            },
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { isFocused = it.isFocused }
        )

        // Кнопка очистки (если есть текст)
        if (searchText.isNotEmpty()) {
            IconButton(
                onClick = { onSearchTextChange("") },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Очистить",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Иконка настроек
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .size(56.dp)
                .padding(end = 8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.setting), // или Icons.Default.Settings
                contentDescription = "Настройки поиска",
                tint = Color(0xFF6AA26C),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}