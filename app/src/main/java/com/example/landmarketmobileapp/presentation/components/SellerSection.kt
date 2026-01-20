package com.example.landmarketmobileapp.presentation.components

import ReviewItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.models.state.ReviewState

@Composable
fun SellerSection(
    sellerName: String,
    sellerRating: Float,
    sellerImage: String,
    reviewsCount: Int,
    sellerSince: String,
    onContactClick: () -> Unit,
    review: List<ReviewState>
) {
    Column(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .background(Color.Companion.White)
            .padding(16.dp)
            .padding(top = 12.dp)
    ) {
        Text(
            text = "Продавец",
            fontSize = 18.sp,
            fontWeight = FontWeight.Companion.Bold,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            modifier = Modifier.Companion.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.Companion.fillMaxWidth(),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            if (sellerImage == "") {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Продавец",
                    tint = Color.Companion.White,
                    modifier = Modifier.Companion.size(14.dp)
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(sellerImage!!)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Изображение",
                    modifier = Modifier.Companion.size(120.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.Companion.width(16.dp))

            Column(
                modifier = Modifier.Companion.weight(1f)
            ) {
                Text(
                    text = sellerName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )

                Row(
                    modifier = Modifier.Companion.padding(top = 4.dp),
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Рейтинг",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.Companion.size(16.dp)
                    )

                    Spacer(modifier = Modifier.Companion.width(4.dp))

                    Text(
                        text = sellerRating.toString(),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )

                    Text(
                        text = " ($reviewsCount отзывов)",
                        fontSize = 14.sp,
                        color = Color.Companion.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
                    )
                }

                Text(
                    text = "На сайте с $sellerSince года",
                    fontSize = 13.sp,
                    color = Color.Companion.Gray,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    modifier = Modifier.Companion.padding(top = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.Companion.height(12.dp))



        Row(
            modifier = Modifier.Companion.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onContactClick,
                modifier = Modifier.Companion.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6AA26C)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Call,
                        contentDescription = "Позвонить"
                    )
                    Spacer(modifier = Modifier.Companion.width(8.dp))
                    Text("Позвонить")
                }
            }
        }
        Column {
            review.forEach { it ->
                ReviewItem(it)
            }
            Spacer(Modifier.Companion.height(10.dp))
        }

        Spacer(modifier = Modifier.Companion.height(12.dp))
    }
}