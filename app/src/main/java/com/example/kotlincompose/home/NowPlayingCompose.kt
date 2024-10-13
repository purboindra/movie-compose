package com.example.kotlincompose.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.base.State
import com.example.kotlincompose.home.viewmodel.HomeIntent
import com.example.kotlincompose.home.viewmodel.HomeViewModel

@Composable
fun NowPlayingCompose(modifier: Modifier,homeViewModel: HomeViewModel) {
    
    val movieState by homeViewModel.stateNowPlayingModel.collectAsState()
    var hasLoaded by remember {
        mutableStateOf(false)
    }
    
    LaunchedEffect(Unit) {
        if (!hasLoaded) {
            homeViewModel.handleIntent(HomeIntent.NowPlaying())
            hasLoaded = true
        }
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
