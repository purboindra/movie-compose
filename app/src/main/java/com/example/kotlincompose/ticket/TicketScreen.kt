package com.example.kotlincompose.ticket

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.bumble.appyx.navmodel.backstack.operation.push
import com.example.base.State
import com.example.entity.data.Movie
import com.example.kotlincompose.home.rating
import com.example.kotlincompose.ticket.viewmodel.TicketIntent
import com.example.kotlincompose.ticket.viewmodel.TicketViewModel
import com.example.routes.LocalNavBackStack
import com.example.routes.NavTarget
import com.example.utils.imageLoader
import kotlinx.coroutines.launch

@Composable

fun TicketScreen(ticketViewModel: TicketViewModel = viewModel()) {
    
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    
    val movies by ticketViewModel.movies.collectAsState()
    
    val backStack = LocalNavBackStack.current
    
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            ticketViewModel.handleIntent(TicketIntent.FetchMovie(context))
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        when (movies) {
            is State.Idle -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            
            is State.Success -> {
                val items = (movies as State.Success<List<Movie.Result>>).data
                Log.d("Ticket", "$items")
                Text(text = "Your Ticket", fontSize = 18.sp, fontWeight = FontWeight.W600)
                Spacer(modifier = Modifier.height(12.dp))
                LazyColumn {
                    itemsIndexed(items = items) { index, movie ->
                        
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
                                Text(
                                    text = movie.title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
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
                                Spacer(modifier = Modifier.height(5.dp))
                                ElevatedButton(
                                    onClick = {
                                        ticketViewModel.handleIntent(
                                            TicketIntent.DeleteTicket(
                                                context,
                                                movie.id.toString()
                                            )
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                                }
                            }
                        }
                    }
                }
            }
            
            else -> {}
        }
    }
    
}