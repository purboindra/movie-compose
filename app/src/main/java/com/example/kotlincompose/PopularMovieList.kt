package com.example.kotlincompose

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.example.entity.data.Movie
import java.text.DecimalFormat


@SuppressLint("DefaultLocale")
fun rating(rating: Double):String {
    val decimalFormat = DecimalFormat("#.#")
    val formattedRating = decimalFormat.format(rating)
    return formattedRating
}


@Composable
fun PopularMovieListCompose(
    context: Context = LocalContext.current,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    movies: List<Movie.Result>
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(315.dp)
    ) {
        itemsIndexed(movies) { _, item ->
            
            val imageLoader = ImageLoader.Builder(context)
                .memoryCache {
                    MemoryCache.Builder(context)
                        .maxSizePercent(0.25)
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .directory(context.cacheDir.resolve("image_cache"))
                        .maxSizePercent(0.02)
                        .build()
                }
                .build()
            
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 4.dp)
                    .width(145.dp),
            ) {
                Surface (shadowElevation = 7.dp, shape = RoundedCornerShape(8.dp), modifier = Modifier
                    .fillMaxWidth()
                    .height(215.dp),) {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w500/${item.posterPath}",
                        contentDescription = "Image: ${item.title}",
                        imageLoader = imageLoader,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = item.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC319),
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = rating(item.voteAverage), color = Color(0xff9C9C9C), fontSize = 13.sp)
                }
            }
        }
    }
}
