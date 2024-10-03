package com.example.kotlincompose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.NodeActivity
import com.example.kotlincompose.ui.theme.KotlinComposeTheme
import com.example.routes.NavTarget
import com.example.routes.RootNode

class MainActivity : NodeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            
            val items = listOf(
                NavTarget.Child1,
                NavTarget.Child2,
                NavTarget.Child3
            )
            
            // State to hold selected item
            var selectedItem by remember { mutableStateOf(0) }
            
            KotlinComposeTheme {
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            items = items,
                            selectedItem = selectedItem,
                            onItemSelected = { index ->
                                selectedItem = index
                             
                            }
                        )
                    }
                ) { innerPadding ->
                    NodeHost(integrationPoint = appyxV1IntegrationPoint) {
                        RootNode(buildContext = it)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<NavTarget>,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    BottomNavigation {
        items.forEachIndexed { index, item ->
            BottomNavigationItem(
                icon = {
                    when (item) {
                        is NavTarget.Child1 -> Icon(Icons.Default.Home, contentDescription = "Home")
                        is NavTarget.Child2 -> Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = "Movie"
                        )
                        
                        is NavTarget.Child3 -> Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile"
                        )
                    }
                },
                label = { Text("Test") },
                selected = selectedItem == index,
                onClick = { onItemSelected(index) }
            )
        }
    }
}

