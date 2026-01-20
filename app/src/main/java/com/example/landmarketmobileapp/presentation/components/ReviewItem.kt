
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.models.state.ReviewState

@Composable
fun ReviewItem(review: ReviewState) {
    Column {
        // Заголовок отзыва
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = review.username!!,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StarRating(
                        rating = review.rating,
                        maxStars = 5,
                        starSize = 16.dp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = review.createdAt,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
                    )
                }
            }

            // Кнопки действий
            Row {
                IconButton(
                    onClick = { /* Полезно */ },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.ThumbUp,
                        contentDescription = "Полезно",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = { /* Пожаловаться */ },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "Пожаловаться",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Текст отзыва
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = review.comment,
            fontSize = 15.sp,
            lineHeight = 20.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            color = Color.DarkGray
        )

        // Счетчик полезности
        if (review.helpful_count > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${review.helpful_count} человек считают этот отзыв полезным",
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )
        }

        // Разделитель
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
    }
}

@Composable
fun StarRating(
    rating: Float,
    maxStars: Int = 5,
    starSize: Dp = 20.dp,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        for (i in 1..maxStars) {
            val starFill = when {
                rating >= i -> 1f
                rating > i - 1 -> rating - (i - 1)
                else -> 0f
            }

            Box(modifier = Modifier.size(starSize)) {
                // Фоновая звезда (пустая)
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.fillMaxSize()
                )

                // Заполненная звезда
                if (starFill > 0) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                alpha = starFill
                            }
                    )
                }
            }
        }
    }
}