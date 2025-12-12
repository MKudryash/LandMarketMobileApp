package com.example.landmarketmobileapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R

@Composable
fun MainHeader(
    title: String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF338035).copy(alpha = 0.85f),
                        Color(0xFF338035).copy(alpha = 0.65f)
                    )
                ),
                shape = RoundedCornerShape(10.dp
                )
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
    {
        Text(text =title,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            fontSize = 48.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_semimold)),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
@Composable
fun SecondMainHeader(
    title: String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF338035).copy(alpha = 0.85f),
                        Color(0xFF338035).copy(alpha = 0.65f)
                    )
                ),
                shape = RoundedCornerShape(10.dp
                )
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
    {
        Text(text =title,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_semimold)),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SecondHeader(
    title: String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF338035).copy(alpha = 0.85f),
                        Color(0xFF338035).copy(alpha = 0.65f)
                    )
                ),
                shape = RoundedCornerShape(10.dp
                )
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )  {
        Text(
            text =title,
            color = Color.White,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_semimold)),
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp)
        )
    }
}

@Composable
fun ThirdHeader(
    title: String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth(0.6f)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF338035).copy(alpha = 0.85f),
                        Color(0xFF338035).copy(alpha = 0.65f)
                    )
                ),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 10.dp,
                    bottomEnd = 10.dp,
                    bottomStart = 0.dp
                )
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )  {
        Text(
            text =title,
            color = Color.White,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp)
        )
    }
}

@Composable
fun MiniHeader(
    title: String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth(0.6f)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.85f),
                        Color.White.copy(alpha = 0.65f)
                    )
                ),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 20.dp,
                    bottomEnd = 20.dp,
                    bottomStart = 0.dp
                )
            )
            .padding(vertical = 12.dp)
    )  {
        Text(
            text =title,
            color = Color(0xFF338035),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp)
        )
    }
}

@Preview
@Composable
fun MainHeaderPreview(){
    Column() {
        MainHeader("О НАС")
        SecondHeader("Поселки в Нижегородской области")
        ThirdHeader("Смотреть все поселки")
        MiniHeader(" от 600 000 Р")
    }
}