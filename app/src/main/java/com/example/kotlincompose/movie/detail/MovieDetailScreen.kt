package com.example.kotlincompose.movie.detail

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.base.State
import com.example.kotlincompose.AppIntent
import com.example.kotlincompose.AppViewModel

@Composable
fun MovieDetailScreen(id: String, modifier: Modifier, appViewModel: AppViewModel = viewModel()) {
    
    val movieState by appViewModel.stateDetailMovieModel.collectAsState()
    
    LaunchedEffect(Unit) {
        appViewModel.handleIntent(AppIntent.DetailMovie(id))
    }
    
    when (val state = movieState.detailMovieState) {
        is State.Idle -> {
        
        }
        
        is State.Failure -> {
        
        }
        
        is State.Loading -> {
            Scaffold { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        
        is State.Success -> {
            val movie = state.data
            Scaffold(topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Detail Movie",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }) { innerPadding ->
                LazyColumn(modifier = modifier.padding(innerPadding)) {
                    item {
                        Text(text = "Hello world: ${id}", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}