package com.example.landmarketmobileapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R

@Composable
fun FeaturesSection(features: List<String>) {
    Column(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .background(Color.Companion.White)
            .padding(16.dp)
            .padding(top = 12.dp)
    ) {
        Text(
            text = "Особенности участка",
            fontSize = 18.sp,
            fontWeight = FontWeight.Companion.Bold,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            modifier = Modifier.Companion.padding(bottom = 12.dp)
        )

        FlowRow(
            modifier = Modifier.Companion.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            features.forEach { feature ->
                Surface(
                    modifier = Modifier.Companion
                        .clip(RoundedCornerShape(8.dp)),
                    color = Color(0xFF6AA26C).copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, Color(0xFF6AA26C).copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.Companion.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.Companion.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Особенность",
                            tint = Color(0xFF6AA26C),
                            modifier = Modifier.Companion.size(16.dp)
                        )

                        Spacer(modifier = Modifier.Companion.width(6.dp))

                        Text(
                            text = feature,
                            fontSize = 13.sp,
                            color = Color(0xFF6AA26C),
                            fontFamily = FontFamily(Font(R.font.montserrat_medium))
                        )
                    }
                }
            }
        }
    }
}