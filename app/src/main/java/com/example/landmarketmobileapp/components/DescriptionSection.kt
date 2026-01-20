package com.example.landmarketmobileapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.viewModels.AdvertisementState

@Composable
fun DescriptionSection(advertisement: AdvertisementState) {
    Column(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .background(Color.Companion.White)
            .padding(16.dp)
            .padding(top = 12.dp)
    ) {
        Text(
            text = "Описание",
            fontSize = 18.sp,
            fontWeight = FontWeight.Companion.Bold,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            modifier = Modifier.Companion.padding(bottom = 12.dp)
        )

        Text(
            text = advertisement.description,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            color = Color.Companion.DarkGray
        )

        if (advertisement.additionalInfo.isNotEmpty()) {
            Spacer(modifier = Modifier.Companion.height(12.dp))

            Surface(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                color = Color(0xFF6AA26C).copy(alpha = 0.1f)
            ) {
                Column(
                    modifier = Modifier.Companion.padding(12.dp)
                ) {
                    Text(
                        text = "Дополнительная информация",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Companion.Bold,
                        color = Color(0xFF6AA26C),
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )

                    Spacer(modifier = Modifier.Companion.height(4.dp))

                    Text(
                        text = advertisement.additionalInfo,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        color = Color.Companion.DarkGray
                    )
                }
            }
        }
    }
}