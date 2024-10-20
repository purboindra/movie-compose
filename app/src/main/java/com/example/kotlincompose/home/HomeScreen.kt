package com.example.kotlincompose.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumble.appyx.navmodel.backstack.operation.push
import com.example.kotlincompose.home.viewmodel.HomeViewModel
import com.example.routes.LocalNavBackStack
import com.example.routes.NavTarget

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    
    val backStack = LocalNavBackStack.current
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        item {
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                
                Spacer(modifier = Modifier.height(5.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Find your favorite movie",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search Icon",
                        modifier = Modifier.clickable {
                            backStack.push(NavTarget.Search)
                        })
                }
                Spacer(modifier = Modifier.height(15.dp))
                // PASS VIEW MODEL AS PARAMATER
                CategoryCompose(homeViewModel)
                Spacer(modifier = Modifier.height(15.dp))
                
                PopularMovieCompose(Modifier.padding(2.dp), homeViewModel)
                
                Spacer(modifier = Modifier.height(15.dp))
                NowPlayingCompose(Modifier.padding(2.dp), homeViewModel)
            }
        }
    }
}