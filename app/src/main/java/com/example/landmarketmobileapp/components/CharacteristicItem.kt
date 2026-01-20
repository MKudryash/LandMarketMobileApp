package com.example.landmarketmobileapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R

@Composable
fun CharacteristicItem(
    icon: ImageVector,
    value: String,
    label: String,
) {
    Column(horizontalAlignment = Alignment.Companion.CenterHorizontally) {
        Row(verticalAlignment = Alignment.Companion.CenterVertically) {
            Icon(
                icon,
                contentDescription = label,
                tint = Color(0xFF6AA26C),
                modifier = Modifier.Companion.size(18.dp)
            )

            Spacer(modifier = Modifier.Companion.width(4.dp))

            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Companion.Bold,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )
        }

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Companion.Gray,
            fontFamily = FontFamily(Font(R.font.montserrat_regular))
        )
    }
}