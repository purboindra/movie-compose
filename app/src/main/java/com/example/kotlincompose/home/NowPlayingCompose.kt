package com.example.kotlincompose.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.example.base.State
import com.example.entity.data.Movie
import com.example.kotlincompose.AppIntent
import com.example.kotlincompose.AppViewModel

@Composable
fun NowPlayingCompose(modifier: Modifier, appViewModel: AppViewModel = viewModel()) {
    
    val movieState by appViewModel.stateNowPlayingModel.collectAsState()
    
    LaunchedEffect(Unit) {
        println("LaunchedEffect NowPlayingCompose")
        appViewModel.handleIntent(AppIntent.NowPlaying)
        println("movieState NowPlayingCompose: ${movieState.nowPlayingResponseState}")
    }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(820.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Now Playing",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF110E47),
                fontSize = 24.sp
            )
            Text(text = "See more", fontSize = 12.sp, color = Color(0xFFAAA9B1))
        }
        Spacer(modifier = Modifier.height(18.dp))
        when (val state = movieState.nowPlayingResponseState) {
            is State.Idle -> {}
            is State.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            is State.Success -> {
                val movie = state.data
                val fiveMovies = movie.results.take(5)
                NowPlayingMovieList(movies = fiveMovies)
            }
            
            is State.Failure -> {
                val throwable = state.throwable
                // TODO ERROR HANDLING COMPSOE
            }
        }
    }
}

@Composable
fun NowPlayingMovieList(
    context: Context = LocalContext.current,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    movies: List<Movie.Result>
) {
    
    LazyColumn {
        itemsIndexed(movies) { _, movie ->
            Row(
                modifier = Modifier
                    .height(156.dp)
                    .padding(vertical = 8.dp)
            ) {
                
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
                        imageLoader = imageLoader,
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