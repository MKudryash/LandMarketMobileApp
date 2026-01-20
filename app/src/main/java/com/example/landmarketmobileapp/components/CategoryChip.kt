package com.example.landmarketmobileapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.models.Category

@Composable
fun CategoryChip(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.Companion
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = if (isSelected) Color(0xFF6AA26C) else Color.Companion.White,
        tonalElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier.Companion.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Companion.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                painter = painterResource(id = category.iconResId),
                contentDescription = category.name,
                tint = if (isSelected) Color.Companion.White else Color(0xFF6AA26C),
                modifier = Modifier.Companion.size(16.dp)
            )

            Text(
                text = category.name,
                fontSize = 12.sp,
                color = if (isSelected) Color.Companion.White else Color.Companion.Black,
                fontFamily = FontFamily(Font(R.font.montserrat_medium))
            )
        }
    }
}