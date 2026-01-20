package com.example.landmarketmobileapp.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R

@Composable
fun ProfileField(
    icon: ImageVector,
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType
) {
    Row(
        verticalAlignment = Alignment.Companion.CenterVertically,
        modifier = Modifier.Companion.fillMaxWidth()
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = Color(0xFF6AA26C),
            modifier = Modifier.Companion.size(24.dp)
        )

        Spacer(modifier = Modifier.Companion.width(16.dp))

        Column(modifier = Modifier.Companion.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Companion.Gray,
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )

            if (isEditing) {
                AuthTextField(
                    value = value,
                    onValueChange = onValueChange,
                    label = "",
                    keyboardType = keyboardType,
                    modifier = Modifier.Companion.padding(top = 4.dp)
                )
            } else {
                Text(
                    text = value,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    modifier = Modifier.Companion.padding(top = 4.dp)
                )
            }
        }
    }
}