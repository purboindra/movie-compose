package com.example.kotlincompose.movie.detail

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import android.content.Context
import android.content.res.Resources.Theme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Chip
import androidx.compose.material.Colors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.bumble.appyx.navmodel.backstack.operation.pop
import com.example.base.State
import com.example.kotlincompose.AppIntent
import com.example.kotlincompose.AppViewModel
import com.example.kotlincompose.R
import com.example.kotlincompose.home.rating
import com.example.routes.LocalNavBackStack
import com.example.utils.formatReleaseDate
import com.example.utils.imageLoader

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
fun MovieDetailScreen(id: String, modifier: Modifier, appViewModel: AppViewModel = viewModel()) {
    
    val movieState by appViewModel.stateDetailMovieModel.collectAsState()
    val backStack = LocalNavBackStack.current
    
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        appViewModel.handleIntent(AppIntent.DetailMovie(id))
    }
    
    Scaffold ( modifier = Modifier.statusBarsPadding(), topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Movie",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { backStack.pop() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            backgroundColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.statusBarsPadding()
        )
    }) { innerPadding ->
        when (val state = movieState.detailMovieState) {
            is State.Idle -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(60.dp)
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            is State.Failure -> {
                val error = state.throwable
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(60.dp)
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "${error.message} -- ${id}" ?: "Something went wrong")
                }
            }
            
            is State.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(60.dp)
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            is State.Success -> {
                val movie = state.data
                LazyColumn(modifier = Modifier.padding(horizontal = 12.dp)) {
                    item {
                        Surface(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(300.dp)
                        ) {
                            AsyncImage(
                                model = "https://image.tmdb.org/t/p/w500/${movie.posterPath}",
                                contentDescription = movie.title,
                                imageLoader = imageLoader(context = context),
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        
                        Box(modifier = Modifier.height(24.dp))
                        
                        Text(
                            text = movie.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xff334BFB)
                        )
                        
                        Box(modifier = Modifier.height(9.dp))
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_access_time_24),
                                    contentDescription = "Runtime",
                                    modifier = Modifier.size(18.dp),
                                    tint = Color(0xff858585)
                                )
                                Box(modifier = Modifier.width(2.dp))
                                Text(text = movie.runtime.toString(), color = Color(0xff858585))
                            }
                            Box(modifier = Modifier.width(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = "Rating",
                                    modifier = Modifier.size(18.dp),
                                    tint = Color(0xff858585)
                                )
                                Box(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "${rating(movie.voteAverage)} (IMDB)",
                                    color = Color(0xff858585)
                                )
                            }
                        }
                        
                        Box(modifier = Modifier.height(15.dp))
                        
                        Box(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(1.dp)
                                .background(color = Color(0xff858585))
                        )
                        
                        Box(modifier = Modifier.height(15.dp))
                        
                        Row {
                            Column {
                                Text(
                                    text = "Release Date",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xff858585)
                                )
                                Box(modifier = Modifier.height(5.dp))
                                Text(
                                    text = formatReleaseDate(movie.releaseDate),
                                    color = Color(0xff858585)
                                )
                            }
                            
                            Box(modifier = Modifier.width(28.dp))
                            
                            Column {
                                Text(
                                    text = "Genres",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xff858585)
                                )
                                Box(modifier = Modifier.height(5.dp))
                                FlowRow() {
                                    movie.genres.forEach { genre ->
                                        Chip(
                                            onClick = { },
                                            modifier = Modifier.padding(horizontal = 3.dp),
                                            enabled = false
                                        ) {
                                            Text(text = genre.name)
                                        }
                                    }
                                }
                            }
                        }
                        
                        Box(modifier = Modifier.height(15.dp))
                        
                        Box(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(1.dp)
                                .background(color = Color(0xff858585))
                        )
                        Box(modifier = Modifier.height(15.dp))
                        
                        Text(
                            text = "Synopsis",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xff858585)
                        )
                        Box(modifier = Modifier.height(5.dp))
                        
                        Text(
                            text = movie.overview,
                        )
                        Box(modifier = Modifier.height(15.dp))
                        Button(
                            onClick = { },
                            shape = RoundedCornerShape(8.dp),
                            modifier = modifier.fillMaxWidth(),
                        ) {
                            Text("Buy Ticket", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        }
                        
                    }
                }
            }
        }
    }
    
    
}