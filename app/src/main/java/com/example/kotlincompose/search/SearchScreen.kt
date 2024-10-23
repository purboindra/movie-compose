package com.example.kotlincompose.search

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.bumble.appyx.navmodel.backstack.operation.pop
import com.bumble.appyx.navmodel.backstack.operation.push
import com.example.base.State
import com.example.entity.data.Movie
import com.example.kotlincompose.home.rating
import com.example.kotlincompose.search.viewmodel.SearchViewModel
import com.example.routes.LocalNavBackStack
import com.example.routes.NavTarget
import com.example.utils.formatReleaseDate
import com.example.utils.imageLoader
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SearchScreen(searchViewModel: SearchViewModel = viewModel()) {

    val backStack = LocalNavBackStack.current

    val searchState by searchViewModel.searchMovieState.collectAsState()

    val query by searchViewModel.searchQuery.collectAsState()

    val context = LocalContext.current

//    LaunchedEffect(key1 = backStack.screenState.value) {
//        searchViewModel.observeSearchQuery()
//    }

    DisposableEffect(key1 = backStack) {
        onDispose {
            searchViewModel.resetSearchState()
            Log.d(
                "dispose",
                "${backStack.screenState.value.offScreen} === ${backStack.screenState.value.offScreen}"
            )
        }
    }

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
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is State.Success -> {
                    val movies = (searchState as State.Success<Movie>).data.results
                    LazyColumn {
                        itemsIndexed(movies) { _, movie ->
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White,
                                ),
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .padding(vertical = 8.dp),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 2.dp
                                ),
                                onClick = {
                                    backStack.push(NavTarget.MovieDetail(movie.id.toString()))
                                },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    )
                                ) {
                                    Row {
                                        AsyncImage(
                                            model = "https://image.tmdb.org/t/p/w500/${movie.posterPath}",
                                            contentDescription = movie.title,
                                            modifier = Modifier
                                                .width(64.dp)
                                                .height(64.dp)
                                                .clip(RoundedCornerShape(8.dp)),
                                            contentScale = ContentScale.Crop,
                                            imageLoader = imageLoader(context = context)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(text = movie.title)
                                            Spacer(modifier = Modifier.height(5.dp))
                                            Text(
                                                text = movie.releaseDate?.let {
                                                    formatReleaseDate(
                                                        it
                                                    )
                                                }.orEmpty(),
                                                color = Color(0xff858585)
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
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
                    }
                }

                is State.Failure -> {
                    val error = (searchState as State.Failure).throwable
                    Text(text = error.message.orEmpty())
                }

                else -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Movie not found")
                    }
                }
            }
        }
    }
}