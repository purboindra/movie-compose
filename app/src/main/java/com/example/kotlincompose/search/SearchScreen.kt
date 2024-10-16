package com.example.kotlincompose.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Colors
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumble.appyx.navmodel.backstack.operation.pop
import com.example.base.State
import com.example.entity.data.Movie
import com.example.kotlincompose.search.viewmodel.SearchViewModel
import com.example.routes.LocalNavBackStack

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SearchScreen(searchViewModel: SearchViewModel = viewModel()) {
    
    val backStack = LocalNavBackStack.current
    
    val searchState by searchViewModel.searchMovieState.collectAsState()
    
    val query by searchViewModel.searchQuery.collectAsState()
    
    Scaffold(topBar = {
        TopAppBar(
            modifier = Modifier.statusBarsPadding(),
            elevation = 0.dp,
            backgroundColor = Color.White,
            title = { Text(text = "Search", fontSize = 18.sp, fontWeight = FontWeight.Medium) },
            navigationIcon = {
                IconButton(onClick = {
                    backStack.pop()
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            
            )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 18.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = query.orEmpty(),
                onValueChange = { query ->
                    searchViewModel.onSearchQueryChange(query)
                },
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(text = "Search movie") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                shape = RoundedCornerShape(12.dp),
                textStyle = TextStyle(fontSize = 18.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                )
            )
            
            when (searchState) {
                is State.Loading -> {
                    CircularProgressIndicator()
                }
                
                is State.Success -> {
                    val movies = (searchState as State.Success<Movie>).data.results
                    Text(text = "Hello9")
                }
                
                is State.Failure -> {
                    val error = (searchState as State.Failure).throwable
                    Text(text = error.message.orEmpty())
                }
                
                else -> {
                    // Handle idle or other states if necessary
                }
            }
        }
    }
}