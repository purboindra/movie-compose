package com.example.kotlincompose.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import coil.compose.AsyncImage
import com.bumble.appyx.navmodel.backstack.operation.push
import com.example.entity.data.Movie
import com.example.routes.LocalNavBackStack
import com.example.routes.NavTarget
import com.example.utils.imageLoader


@Composable
fun NowPlayingMovieList(
    context: Context = LocalContext.current,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    movies: List<Movie.Result>
) {
    val backStack = LocalNavBackStack.current
    
    LazyColumn {
        itemsIndexed(movies) { _, movie ->
            Row(
                modifier = Modifier
                    .height(156.dp)
                    .padding(vertical = 8.dp)
                    .clickable {
                        backStack.push(NavTarget.MovieDetail(movie.id.toString()))
                    }
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(100.dp)
                ) {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w500/${movie.posterPath}",
                        contentDescription = movie.title,
                        imageLoader = imageLoader(context),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(text = movie.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFC319),
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = rating(movie.voteAverage),
                            color = Color(0xff9C9C9C),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
    
}