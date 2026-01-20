package com.example.landmarketmobileapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R

@Composable
fun AuthTextLink(
    prefixText: String,
    linkText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.Companion
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Companion.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = prefixText,
            fontSize = 16.sp,
            color = Color.Companion.Gray,
            fontFamily = FontFamily(Font(R.font.montserrat_regular))
        )
        TextButton(
            onClick = onClick,
            modifier = Modifier.Companion.padding(start = 4.dp)
        ) {
            Text(
                text = linkText,
                fontSize = 16.sp,
                color = Color(0xFF6AA26C),
                fontWeight = FontWeight.Companion.SemiBold,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )
        }
    }
}