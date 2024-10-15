package com.example.kotlincompose.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import com.example.base.State
import com.example.kotlincompose.home.viewmodel.HomeIntent
import com.example.kotlincompose.home.viewmodel.HomeViewModel
import com.example.kotlincompose.loading.LoadingShimmer


@Composable
fun CategoryCompose(homeViewModel: HomeViewModel) {
    
    val categoryState by homeViewModel.stateCategoryModel.collectAsState()
    
    val categoryIndex by homeViewModel.categoryIndex.collectAsState()
    
    var hasLoaded = false
    
    LaunchedEffect(Unit) {
        if (!hasLoaded) {
            homeViewModel.handleIntent(HomeIntent.Categories)
            hasLoaded = true
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp),
    ) {
        when (val state = categoryState.categoryResponseState) {
            is State.Idle -> {
                LoadingShimmer(height = 48.dp)
            }
            
            is State.Loading -> {
                LoadingShimmer(height = 48.dp)
            }
            
            is State.Success -> {
                val data = state.data
                
                LazyRow(modifier = Modifier.fillMaxHeight()) {
                    itemsIndexed(data.genres) { index, item ->
                        Surface(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(horizontal = 6.dp)
                                .clickable {
                                    homeViewModel.onTapCategory(item.id.toString())
                                    homeViewModel.onTapCategoryIndex(index)
                                },
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xff334BFB)),
                            color = if (categoryIndex == index) {
                                Color(0xff334BFB)
                            } else {
                                Color.Transparent
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.name,
                                    color = if (categoryIndex == index) Color.White else Color.Black,
                                    fontWeight = FontWeight.Medium,
                                )
                            }
                        }
                    }
                }
            }
            
            is State.Failure -> {
                val throwable = state.throwable
            }
        }
    }
}