
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.protobuf.Internal
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.components.SearchBarWithSettings
import com.example.landmarketmobileapp.screens.Review
import com.example.landmarketmobileapp.viewModels.AdvertisementState
import com.example.landmarketmobileapp.viewModels.AdvertisementViewModel
import com.example.landmarketmobileapp.viewModels.ReviewState
import kotlinx.coroutines.delay
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

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

@Composable
fun RatingDistribution(reviews: List<Review>) {
    val total = reviews.size
    val distribution = (5 downTo 1).map { rating ->
        val count = reviews.count { it.rating.toInt() == rating }
        Pair(rating, count)
    }

    Column {
        distribution.forEach { (rating, count) ->
            val percentage = if (total > 0) (count.toFloat() / total * 100) else 0f

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Рейтинг
                Text(
                    text = "$rating",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                    modifier = Modifier.width(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Звезда
                Icon(
                    Icons.Default.Star,
                    contentDescription = "$rating звезд",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Прогресс-бар
                LinearProgressIndicator(
                    progress = { percentage / 100 },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp),
                    color = Color(0xFF6AA26C),
                    trackColor = Color.Gray.copy(alpha = 0.2f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Процент
                Text(
                    text = "${String.format("%.0f", percentage)}%",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                    modifier = Modifier.width(32.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyReviewsState() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Star,
            contentDescription = "Нет отзывов",
            tint = Color.Gray.copy(alpha = 0.5f),
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Отзывов пока нет",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            fontFamily = FontFamily(Font(R.font.montserrat_medium))
        )

        Text(
            text = "Будьте первым, кто поделится своим опытом",
            fontSize = 14.sp,
            color = Color.Gray,
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Написать первый отзыв */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6AA26C)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Написать отзыв",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Написать первый отзыв")
            }
        }
    }
}

enum class ReviewSort {
    RECENT, HIGHEST, LOWEST, HELPFUL
}

@Composable
fun ReviewSortChips(
    selectedSort: ReviewSort,
    onSortSelected: (ReviewSort) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ReviewSort.entries.forEach { sort ->
            FilterChip(
                selected = selectedSort == sort,
                onClick = { onSortSelected(sort) },
                label = {
                    Text(
                        text = when (sort) {
                            ReviewSort.RECENT -> "Сначала новые"
                            ReviewSort.HIGHEST -> "Высокий рейтинг"
                            ReviewSort.LOWEST -> "Низкий рейтинг"
                            ReviewSort.HELPFUL -> "Полезные"
                        },
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_medium))
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF6AA26C),
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

// Функция для парсинга даты (упрощенная)
fun parseDate(dateString: String): Long {
    return try {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).parse(dateString)?.time ?: 0
    } catch (e: Exception) {
        0
    }
}

// Функция для расчета статистики оценок
fun calculateRatingStats(reviews: List<Review>): Map<Int, Int> {
    return reviews.groupingBy { it.rating.toInt() }
        .eachCount()
        .toSortedMap(Comparator.reverseOrder())
}