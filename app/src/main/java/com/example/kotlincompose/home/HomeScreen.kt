package com.example.kotlincompose.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    
    val categories = listOf("All Movie", "Action", "Romance", "Comedy")
    
    var categoryIndex by remember {
        mutableIntStateOf(0)
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        item {
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Find your favorite movie",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
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
                Spacer(modifier = Modifier.height(15.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp),
                ) {
                    LazyRow(modifier = Modifier.fillMaxHeight()) {
                        itemsIndexed(categories) { index, item ->
                            Surface(
                                modifier = Modifier
                                    .width(100.dp)
                                    .fillMaxHeight()
                                    .padding(horizontal = 6.dp)
                                    .clickable {
                                        categoryIndex = index
                                    },
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xff334BFB)),
                                color = if (categoryIndex == index) Color(0xff334BFB) else Color.Transparent,
                                content = {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = item,
                                            color = if (categoryIndex == index) Color.White else Color.Black,
                                            fontWeight = FontWeight.Medium,
                                        )
                                    }
                                })
                        }
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                
                PopularMovieCompose(Modifier.padding(2.dp))
                
                Spacer(modifier = Modifier.height(15.dp))
                NowPlayingCompose(Modifier.padding(2.dp))
            }
        }
    }
    
}