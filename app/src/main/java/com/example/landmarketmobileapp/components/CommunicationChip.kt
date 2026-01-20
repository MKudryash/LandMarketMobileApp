package com.example.landmarketmobileapp.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R

@Composable
fun CommunicationChip(
    icon: ImageVector,
    text: String
) {
    Surface(
        modifier = Modifier.Companion
            .clip(RoundedCornerShape(8.dp)),
        color = Color(0xFF6AA26C).copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.Companion.padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = text,
                tint = Color(0xFF6AA26C),
                modifier = Modifier.Companion.size(12.dp)
            )

            Spacer(modifier = Modifier.Companion.width(4.dp))

            Text(
                text = text,
                fontSize = 10.sp,
                color = Color(0xFF6AA26C),
                fontFamily = FontFamily(Font(R.font.montserrat_medium))
            )
        }
    }
}