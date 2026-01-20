package com.example.landmarketmobileapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ItemAboutUs(
    number: Int,
    text: String,
) {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
    Row(
        verticalAlignment = Alignment.Companion.CenterVertically,
        modifier = Modifier.Companion.padding(vertical = 4.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.more),
            tint = Color(0xFF6AA26C),
            contentDescription = null,
            modifier = Modifier.Companion.size(24.dp)
        )
        Spacer(Modifier.Companion.width(8.dp))
        Column {
            Text(
                formatter.format(number),
                color = Color(0xFF6AA26C),
                fontWeight = FontWeight.Companion.Bold,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )
            Text(
                text,
                color = Color.Companion.Black,
                fontWeight = FontWeight.Companion.Normal,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )
        }
    }
}