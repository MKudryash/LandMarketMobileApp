package com.example.landmarketmobileapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.viewModels.Region
import com.example.landmarketmobileapp.viewModels.RegionUI
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CardVillage(
    imageUrl: String,
    title: String = "Название деревни",
    costOfThePlot: Int,
    costPerHundred: Int,
    address: String,
) {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
    Box(
        modifier = Modifier
            .size(width = 260.dp, height = 390.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray)
    ) {
        // Фоновое изображение
        AsyncImage(
            model = imageUrl,
            contentDescription = "Village background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            placeholder = painterResource(id = R.drawable.town),
            error = painterResource(id = R.drawable.town)
        )

        // Градиентный оверлей для лучшей читаемости текста
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        ),
                        startY = 0.6f
                    )
                )
        )
        Column() {
            Text(
                title,
                color = Color.White,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                MiniHeader(
                    " от ${formatter.format(costOfThePlot)} Р",
                    modifier = Modifier.fillMaxWidth(0.55f)
                )
                Text(
                    "Стоимость участка",
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                )
            }
            Spacer(Modifier.height(7.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                MiniHeader(
                    " от ${formatter.format(costPerHundred)} Р",
                    modifier = Modifier.fillMaxWidth(0.55f)
                )
                Text(
                    "Цена за сотку",
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                )


            }
            Spacer(Modifier.weight(1f))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Text(
                    address,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(horizontal = 10.dp)
                )
            }
        }
    }
}
@Composable
fun CardRegion(
    region: RegionUI
) {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
    Box(
        modifier = Modifier
            .size(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray)
    ) {
        // Фоновое изображение
        AsyncImage(
            model = region.imageUrl,
            contentDescription = "Village background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            placeholder = painterResource(id = R.drawable.town),
            error = painterResource(id = R.drawable.town)
        )

        // Градиентный оверлей
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        ),
                        startY = 0.6f
                    )
                )
        )

        // Контент карточки
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                region.name,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            Text(
                region.address,
                color = Color.White,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )

            // Spacer для выталкивания MiniHeader вниз
            Spacer(modifier = Modifier.weight(1f))

            MiniHeader(
                title = " от ${formatter.format(region.costPerHundred)} Р",
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(bottom = 10.dp)
            )
        }
    }
}



@Preview
@Composable
fun CardVillagePreview() {
    CardVillage("", "Бахтаево PARK", 600000, 150000, "Егорьевское ш. 45 км.")
}

