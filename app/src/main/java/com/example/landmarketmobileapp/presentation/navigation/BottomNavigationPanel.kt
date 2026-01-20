package com.example.landmarketmobileapp.presentation.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R

@Composable
fun BottomNavigationPanel(
    selectedItem: NavItem,
    onItemSelected: (NavItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
            .background(Color.Transparent),
        shape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color.DarkGray
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White), // Повторяем фон,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            // Первый элемент (Домой)
            NavigationItem(
                item = NavItem.Home,
                selected = selectedItem == NavItem.Home,
                onClick = { onItemSelected(NavItem.Home) },
                icon = painterResource( R.drawable.search),
                title = NavItem.Home.title
            )

            // Второй элемент (Поиск)
            NavigationItem(
                item = NavItem.Ads,
                selected = selectedItem == NavItem.Ads,
                onClick = { onItemSelected(NavItem.Ads) },
                icon = painterResource( R.drawable.corp_ads),
                title = NavItem.Ads.title
            )

            // Четвертый элемент (Профиль)
            NavigationItem(
                item = NavItem.Messages,
                selected = selectedItem == NavItem.Messages,
                onClick = { onItemSelected(NavItem.Messages) },
                icon = painterResource( R.drawable.message),
                title = NavItem.Messages.title
            )

            // Пятый элемент (Настройки)
            NavigationItem(
                item = NavItem.Profile,
                selected = selectedItem == NavItem.Profile,
                onClick = { onItemSelected(NavItem.Profile) },
                icon = painterResource( R.drawable.user),
                title = NavItem.Profile.title
            )
        }
    }
}

@Composable
fun NavigationItem(
    item: NavItem,
    selected: Boolean,
    onClick: () -> Unit,
    icon: Painter,
    title: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .width(80.dp)
            .height(80.dp)
    ) {


        Box(
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .size(48.dp)
            ) {
                Icon(
                    painter = icon,
                    contentDescription = item.title,
                    tint =if (selected) Color(0xFF6AA26C) else Color.Gray,
                    modifier = Modifier.size(38.dp)
                )

            }
        }
        Text(title,
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
        )
    }
}

@Preview
@Composable
fun MainScreenWithBottomNav() {
    var selectedItem by remember { mutableStateOf<NavItem>(NavItem.Home) }

    Scaffold(
        bottomBar = {
            BottomNavigationPanel(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E1E1E))
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Выбран: ${selectedItem.title}",
                color = Color.White,
                fontSize = 24.sp,
            )
        }
    }
}


// Демонстрация всех вариантов
@Preview
@Composable
fun BottomNavigationPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
    ) {

        BottomNavigationPanel(
            selectedItem = NavItem.Home,
            onItemSelected = {}
        )


    }
}