package com.example.kotlincompose.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.kotlincompose.R
import com.example.kotlincompose.home.HomeScreen

@Composable
fun MainScreen() {
    
    val bodies = listOf<@Composable () -> Unit>(
        { HomeScreen() },
        {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "Placeholder for Cinemas")
            }
        },
        {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "Placeholder for Ticket")
            }
        },
        {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "Placeholder for Profile")
            }
        }
    )
    
    var selectedItem by remember { mutableIntStateOf(0) }
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = bodies.indices.toList(),
                selectedItem = selectedItem,
                onItemSelected = { index ->
                    selectedItem = index
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            bodies[selectedItem]()
        }
    }
}


@Composable
fun BottomNavigationBar(
    items: List<Int>,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    BottomNavigation(backgroundColor = Color(0xffFFFFFF)) {
        items.forEachIndexed { index, item ->
            BottomNavigationItem(
                modifier = Modifier.padding(vertical = 12.dp),
                selected = selectedItem == index,
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Gray,
                icon = {
                    when (item) {
                        0 -> Icon(
                            Icons.Default.Home, contentDescription = "Home",
                            tint = if (selectedItem == index) Color(0xff5D3587) else Color.Gray
                        )
                        
                        1 -> Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = "Movie",
                            tint = if (selectedItem == index) Color(0xff5D3587) else Color.Gray
                        )
                        
                        2 -> Icon(
                            painter = painterResource(id = R.drawable.cinemas_gray),
                            contentDescription = "Ticket",
                            tint = if (selectedItem == index) Color(0xff5D3587) else Color.Gray
                        )
                        
                        3 -> Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = if (selectedItem == index) Color(0xff5D3587) else Color.Gray
                        )
                    }
                },
                label = {
                    Text(
                        color = if (selectedItem == index) Color(0xff5D3587) else Color.Gray,
                        text = when (item) {
                            0 -> "Home"
                            1 -> "Cinemas"
                            2 -> "Ticket"
                            3 -> "Profile"
                            else -> "Unkown"
                        }
                    )
                },
                onClick = {
                    onItemSelected(index)
                }
            )
        }
    }
}

