package com.example.landmarketmobileapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
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

@Composable
fun DocumentsSection(documents: List<String>) {
    Column(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .background(Color.Companion.White)
            .padding(16.dp)
            .padding(top = 12.dp)
    ) {
        Text(
            text = "Документы",
            fontSize = 18.sp,
            fontWeight = FontWeight.Companion.Bold,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            modifier = Modifier.Companion.padding(bottom = 12.dp)
        )

        Column {
            documents.forEachIndexed { index, document ->
                Row(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.corp_ads),
                        contentDescription = "Документ",
                        tint = Color(0xFF6AA26C),
                        modifier = Modifier.Companion.size(20.dp)
                    )

                    Spacer(modifier = Modifier.Companion.width(12.dp))

                    Text(
                        text = document,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        modifier = Modifier.Companion.weight(1f)
                    )
                }

                if (index < documents.size - 1) {
                    Divider(modifier = Modifier.Companion.padding(start = 32.dp))
                }
            }
        }
    }
}